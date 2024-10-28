/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocCategoryDetailComponent from '@/entities/doc-category/doc-category-details.vue';
import DocCategoryClass from '@/entities/doc-category/doc-category-details.component';
import DocCategoryService from '@/entities/doc-category/doc-category.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DocCategory Management Detail Component', () => {
    let wrapper: Wrapper<DocCategoryClass>;
    let comp: DocCategoryClass;
    let docCategoryServiceStub: SinonStubbedInstance<DocCategoryService>;

    beforeEach(() => {
      docCategoryServiceStub = sinon.createStubInstance<DocCategoryService>(DocCategoryService);

      wrapper = shallowMount<DocCategoryClass>(DocCategoryDetailComponent, {
        store,
        localVue,
        router,
        provide: { docCategoryService: () => docCategoryServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocCategory = { id: 123 };
        docCategoryServiceStub.find.resolves(foundDocCategory);

        // WHEN
        comp.retrieveDocCategory(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docCategory).toBe(foundDocCategory);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocCategory = { id: 123 };
        docCategoryServiceStub.find.resolves(foundDocCategory);

        // WHEN
        comp.beforeRouteEnter({ params: { docCategoryId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docCategory).toBe(foundDocCategory);
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
