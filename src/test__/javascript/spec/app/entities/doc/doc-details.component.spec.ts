/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocDetailComponent from '@/entities/doc/doc-details.vue';
import DocClass from '@/entities/doc/doc-details.component';
import DocService from '@/entities/doc/doc.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Doc Management Detail Component', () => {
    let wrapper: Wrapper<DocClass>;
    let comp: DocClass;
    let docServiceStub: SinonStubbedInstance<DocService>;

    beforeEach(() => {
      docServiceStub = sinon.createStubInstance<DocService>(DocService);

      wrapper = shallowMount<DocClass>(DocDetailComponent, {
        store,
        localVue,
        router,
        provide: { docService: () => docServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDoc = { id: 123 };
        docServiceStub.find.resolves(foundDoc);

        // WHEN
        comp.retrieveDoc(123);
        await comp.$nextTick();

        // THEN
        expect(comp.doc).toBe(foundDoc);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDoc = { id: 123 };
        docServiceStub.find.resolves(foundDoc);

        // WHEN
        comp.beforeRouteEnter({ params: { docId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.doc).toBe(foundDoc);
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
