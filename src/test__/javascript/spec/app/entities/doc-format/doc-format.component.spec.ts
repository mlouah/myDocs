/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocFormatComponent from '@/entities/doc-format/doc-format.vue';
import DocFormatClass from '@/entities/doc-format/doc-format.component';
import DocFormatService from '@/entities/doc-format/doc-format.service';
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
  describe('DocFormat Management Component', () => {
    let wrapper: Wrapper<DocFormatClass>;
    let comp: DocFormatClass;
    let docFormatServiceStub: SinonStubbedInstance<DocFormatService>;

    beforeEach(() => {
      docFormatServiceStub = sinon.createStubInstance<DocFormatService>(DocFormatService);
      docFormatServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DocFormatClass>(DocFormatComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          docFormatService: () => docFormatServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      docFormatServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDocFormats();
      await comp.$nextTick();

      // THEN
      expect(docFormatServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docFormats[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      docFormatServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(docFormatServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docFormats[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      docFormatServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(docFormatServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      docFormatServiceStub.retrieve.reset();
      docFormatServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(docFormatServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.docFormats[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      docFormatServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(docFormatServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDocFormat();
      await comp.$nextTick();

      // THEN
      expect(docFormatServiceStub.delete.called).toBeTruthy();
      expect(docFormatServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
