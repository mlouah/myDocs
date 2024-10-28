/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocCollectionUpdateComponent from '@/entities/doc-collection/doc-collection-update.vue';
import DocCollectionClass from '@/entities/doc-collection/doc-collection-update.component';
import DocCollectionService from '@/entities/doc-collection/doc-collection.service';

import DocService from '@/entities/doc/doc.service';

import DocPublisherService from '@/entities/doc-publisher/doc-publisher.service';
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
  describe('DocCollection Management Update Component', () => {
    let wrapper: Wrapper<DocCollectionClass>;
    let comp: DocCollectionClass;
    let docCollectionServiceStub: SinonStubbedInstance<DocCollectionService>;

    beforeEach(() => {
      docCollectionServiceStub = sinon.createStubInstance<DocCollectionService>(DocCollectionService);

      wrapper = shallowMount<DocCollectionClass>(DocCollectionUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docCollectionService: () => docCollectionServiceStub,
          alertService: () => new AlertService(),

          docService: () =>
            sinon.createStubInstance<DocService>(DocService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          docPublisherService: () =>
            sinon.createStubInstance<DocPublisherService>(DocPublisherService, {
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
        comp.docCollection = entity;
        docCollectionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docCollectionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docCollection = entity;
        docCollectionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docCollectionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocCollection = { id: 123 };
        docCollectionServiceStub.find.resolves(foundDocCollection);
        docCollectionServiceStub.retrieve.resolves([foundDocCollection]);

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
