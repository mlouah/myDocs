/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocCategoryComponent from '@/entities/doc-category/doc-category.vue';
import DocCategoryClass from '@/entities/doc-category/doc-category.component';
import DocCategoryService from '@/entities/doc-category/doc-category.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('DocCategory Management Component', () => {
    let wrapper: Wrapper<DocCategoryClass>;
    let comp: DocCategoryClass;
    let docCategoryServiceStub: SinonStubbedInstance<DocCategoryService>;

    beforeEach(() => {
      docCategoryServiceStub = sinon.createStubInstance<DocCategoryService>(DocCategoryService);
      docCategoryServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DocCategoryClass>(DocCategoryComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          docCategoryService: () => docCategoryServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      docCategoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDocCategorys();
      await comp.$nextTick();

      // THEN
      expect(docCategoryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      docCategoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(docCategoryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      docCategoryServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(docCategoryServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      docCategoryServiceStub.retrieve.reset();
      docCategoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(docCategoryServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.docCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      docCategoryServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(docCategoryServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDocCategory();
      await comp.$nextTick();

      // THEN
      expect(docCategoryServiceStub.delete.called).toBeTruthy();
      expect(docCategoryServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
