/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocPublisherComponent from '@/entities/doc-publisher/doc-publisher.vue';
import DocPublisherClass from '@/entities/doc-publisher/doc-publisher.component';
import DocPublisherService from '@/entities/doc-publisher/doc-publisher.service';
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
  describe('DocPublisher Management Component', () => {
    let wrapper: Wrapper<DocPublisherClass>;
    let comp: DocPublisherClass;
    let docPublisherServiceStub: SinonStubbedInstance<DocPublisherService>;

    beforeEach(() => {
      docPublisherServiceStub = sinon.createStubInstance<DocPublisherService>(DocPublisherService);
      docPublisherServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DocPublisherClass>(DocPublisherComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          docPublisherService: () => docPublisherServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      docPublisherServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDocPublishers();
      await comp.$nextTick();

      // THEN
      expect(docPublisherServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docPublishers[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      docPublisherServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(docPublisherServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docPublishers[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      docPublisherServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(docPublisherServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      docPublisherServiceStub.retrieve.reset();
      docPublisherServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(docPublisherServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.docPublishers[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      docPublisherServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(docPublisherServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDocPublisher();
      await comp.$nextTick();

      // THEN
      expect(docPublisherServiceStub.delete.called).toBeTruthy();
      expect(docPublisherServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
