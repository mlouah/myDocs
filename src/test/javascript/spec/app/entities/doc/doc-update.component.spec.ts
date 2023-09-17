/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocUpdateComponent from '@/entities/doc/doc-update.vue';
import DocClass from '@/entities/doc/doc-update.component';
import DocService from '@/entities/doc/doc.service';

import DocPublisherService from '@/entities/doc-publisher/doc-publisher.service';

import DocCollectionService from '@/entities/doc-collection/doc-collection.service';

import DocFormatService from '@/entities/doc-format/doc-format.service';

import LanguageService from '@/entities/language/language.service';

import DocTopicService from '@/entities/doc-topic/doc-topic.service';

import DocAuthorService from '@/entities/doc-author/doc-author.service';

import DocCategoryService from '@/entities/doc-category/doc-category.service';
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
  describe('Doc Management Update Component', () => {
    let wrapper: Wrapper<DocClass>;
    let comp: DocClass;
    let docServiceStub: SinonStubbedInstance<DocService>;

    beforeEach(() => {
      docServiceStub = sinon.createStubInstance<DocService>(DocService);

      wrapper = shallowMount<DocClass>(DocUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docService: () => docServiceStub,
          alertService: () => new AlertService(),

          docPublisherService: () =>
            sinon.createStubInstance<DocPublisherService>(DocPublisherService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          docCollectionService: () =>
            sinon.createStubInstance<DocCollectionService>(DocCollectionService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          docFormatService: () =>
            sinon.createStubInstance<DocFormatService>(DocFormatService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          languageService: () =>
            sinon.createStubInstance<LanguageService>(LanguageService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          docTopicService: () =>
            sinon.createStubInstance<DocTopicService>(DocTopicService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          docAuthorService: () =>
            sinon.createStubInstance<DocAuthorService>(DocAuthorService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          docCategoryService: () =>
            sinon.createStubInstance<DocCategoryService>(DocCategoryService, {
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
        comp.doc = entity;
        docServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.doc = entity;
        docServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDoc = { id: 123 };
        docServiceStub.find.resolves(foundDoc);
        docServiceStub.retrieve.resolves([foundDoc]);

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
