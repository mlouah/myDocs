/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocTopicUpdateComponent from '@/entities/doc-topic/doc-topic-update.vue';
import DocTopicClass from '@/entities/doc-topic/doc-topic-update.component';
import DocTopicService from '@/entities/doc-topic/doc-topic.service';

import DocService from '@/entities/doc/doc.service';

import DomaineService from '@/entities/domaine/domaine.service';
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
  describe('DocTopic Management Update Component', () => {
    let wrapper: Wrapper<DocTopicClass>;
    let comp: DocTopicClass;
    let docTopicServiceStub: SinonStubbedInstance<DocTopicService>;

    beforeEach(() => {
      docTopicServiceStub = sinon.createStubInstance<DocTopicService>(DocTopicService);

      wrapper = shallowMount<DocTopicClass>(DocTopicUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docTopicService: () => docTopicServiceStub,
          alertService: () => new AlertService(),

          docService: () =>
            sinon.createStubInstance<DocService>(DocService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          domaineService: () =>
            sinon.createStubInstance<DomaineService>(DomaineService, {
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
        comp.docTopic = entity;
        docTopicServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docTopicServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docTopic = entity;
        docTopicServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docTopicServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocTopic = { id: 123 };
        docTopicServiceStub.find.resolves(foundDocTopic);
        docTopicServiceStub.retrieve.resolves([foundDocTopic]);

        // WHEN
        comp.beforeRouteEnter({ params: { docTopicId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docTopic).toBe(foundDocTopic);
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
