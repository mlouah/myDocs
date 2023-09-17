/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocPublisherUpdateComponent from '@/entities/doc-publisher/doc-publisher-update.vue';
import DocPublisherClass from '@/entities/doc-publisher/doc-publisher-update.component';
import DocPublisherService from '@/entities/doc-publisher/doc-publisher.service';

import DocCollectionService from '@/entities/doc-collection/doc-collection.service';
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
  describe('DocPublisher Management Update Component', () => {
    let wrapper: Wrapper<DocPublisherClass>;
    let comp: DocPublisherClass;
    let docPublisherServiceStub: SinonStubbedInstance<DocPublisherService>;

    beforeEach(() => {
      docPublisherServiceStub = sinon.createStubInstance<DocPublisherService>(DocPublisherService);

      wrapper = shallowMount<DocPublisherClass>(DocPublisherUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docPublisherService: () => docPublisherServiceStub,
          alertService: () => new AlertService(),

          docCollectionService: () =>
            sinon.createStubInstance<DocCollectionService>(DocCollectionService, {
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
        comp.docPublisher = entity;
        docPublisherServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docPublisherServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docPublisher = entity;
        docPublisherServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docPublisherServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocPublisher = { id: 123 };
        docPublisherServiceStub.find.resolves(foundDocPublisher);
        docPublisherServiceStub.retrieve.resolves([foundDocPublisher]);

        // WHEN
        comp.beforeRouteEnter({ params: { docPublisherId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docPublisher).toBe(foundDocPublisher);
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
