/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocCategoryUpdateComponent from '@/entities/doc-category/doc-category-update.vue';
import DocCategoryClass from '@/entities/doc-category/doc-category-update.component';
import DocCategoryService from '@/entities/doc-category/doc-category.service';

import DocService from '@/entities/doc/doc.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('DocCategory Management Update Component', () => {
    let wrapper: Wrapper<DocCategoryClass>;
    let comp: DocCategoryClass;
    let docCategoryServiceStub: SinonStubbedInstance<DocCategoryService>;

    beforeEach(() => {
      docCategoryServiceStub = sinon.createStubInstance<DocCategoryService>(DocCategoryService);

      wrapper = shallowMount<DocCategoryClass>(DocCategoryUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docCategoryService: () => docCategoryServiceStub,
          alertService: () => new AlertService(),

          docService: () =>
            sinon.createStubInstance<DocService>(DocService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.docCategory = entity;
        docCategoryServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docCategoryServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docCategory = entity;
        docCategoryServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docCategoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocCategory = { id: 123 };
        docCategoryServiceStub.find.resolves(foundDocCategory);
        docCategoryServiceStub.retrieve.resolves([foundDocCategory]);

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
