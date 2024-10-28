/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocTopicComponent from '@/entities/doc-topic/doc-topic.vue';
import DocTopicClass from '@/entities/doc-topic/doc-topic.component';
import DocTopicService from '@/entities/doc-topic/doc-topic.service';
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
  describe('DocTopic Management Component', () => {
    let wrapper: Wrapper<DocTopicClass>;
    let comp: DocTopicClass;
    let docTopicServiceStub: SinonStubbedInstance<DocTopicService>;

    beforeEach(() => {
      docTopicServiceStub = sinon.createStubInstance<DocTopicService>(DocTopicService);
      docTopicServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DocTopicClass>(DocTopicComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          docTopicService: () => docTopicServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      docTopicServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDocTopics();
      await comp.$nextTick();

      // THEN
      expect(docTopicServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docTopics[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      docTopicServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(docTopicServiceStub.retrieve.called).toBeTruthy();
      expect(comp.docTopics[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      docTopicServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(docTopicServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      docTopicServiceStub.retrieve.reset();
      docTopicServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(docTopicServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.docTopics[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      docTopicServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(docTopicServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDocTopic();
      await comp.$nextTick();

      // THEN
      expect(docTopicServiceStub.delete.called).toBeTruthy();
      expect(docTopicServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
