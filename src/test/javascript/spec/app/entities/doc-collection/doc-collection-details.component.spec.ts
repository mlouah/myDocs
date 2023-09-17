/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocCollectionDetailComponent from '@/entities/doc-collection/doc-collection-details.vue';
import DocCollectionClass from '@/entities/doc-collection/doc-collection-details.component';
import DocCollectionService from '@/entities/doc-collection/doc-collection.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DocCollection Management Detail Component', () => {
    let wrapper: Wrapper<DocCollectionClass>;
    let comp: DocCollectionClass;
    let docCollectionServiceStub: SinonStubbedInstance<DocCollectionService>;

    beforeEach(() => {
      docCollectionServiceStub = sinon.createStubInstance<DocCollectionService>(DocCollectionService);

      wrapper = shallowMount<DocCollectionClass>(DocCollectionDetailComponent, {
        store,
        localVue,
        router,
        provide: { docCollectionService: () => docCollectionServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocCollection = { id: 123 };
        docCollectionServiceStub.find.resolves(foundDocCollection);

        // WHEN
        comp.retrieveDocCollection(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docCollection).toBe(foundDocCollection);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocCollection = { id: 123 };
        docCollectionServiceStub.find.resolves(foundDocCollection);

        // WHEN
        comp.beforeRouteEnter({ params: { docCollectionId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docCollection).toBe(foundDocCollection);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
