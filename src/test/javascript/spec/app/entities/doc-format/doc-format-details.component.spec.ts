/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocFormatDetailComponent from '@/entities/doc-format/doc-format-details.vue';
import DocFormatClass from '@/entities/doc-format/doc-format-details.component';
import DocFormatService from '@/entities/doc-format/doc-format.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DocFormat Management Detail Component', () => {
    let wrapper: Wrapper<DocFormatClass>;
    let comp: DocFormatClass;
    let docFormatServiceStub: SinonStubbedInstance<DocFormatService>;

    beforeEach(() => {
      docFormatServiceStub = sinon.createStubInstance<DocFormatService>(DocFormatService);

      wrapper = shallowMount<DocFormatClass>(DocFormatDetailComponent, {
        store,
        localVue,
        router,
        provide: { docFormatService: () => docFormatServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocFormat = { id: 123 };
        docFormatServiceStub.find.resolves(foundDocFormat);

        // WHEN
        comp.retrieveDocFormat(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docFormat).toBe(foundDocFormat);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocFormat = { id: 123 };
        docFormatServiceStub.find.resolves(foundDocFormat);

        // WHEN
        comp.beforeRouteEnter({ params: { docFormatId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docFormat).toBe(foundDocFormat);
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
