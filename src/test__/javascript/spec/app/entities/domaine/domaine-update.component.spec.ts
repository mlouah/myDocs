/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DomaineUpdateComponent from '@/entities/domaine/domaine-update.vue';
import DomaineClass from '@/entities/domaine/domaine-update.component';
import DomaineService from '@/entities/domaine/domaine.service';

import DocTopicService from '@/entities/doc-topic/doc-topic.service';
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
  describe('Domaine Management Update Component', () => {
    let wrapper: Wrapper<DomaineClass>;
    let comp: DomaineClass;
    let domaineServiceStub: SinonStubbedInstance<DomaineService>;

    beforeEach(() => {
      domaineServiceStub = sinon.createStubInstance<DomaineService>(DomaineService);

      wrapper = shallowMount<DomaineClass>(DomaineUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          domaineService: () => domaineServiceStub,
          alertService: () => new AlertService(),

          docTopicService: () =>
            sinon.createStubInstance<DocTopicService>(DocTopicService, {
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
        comp.domaine = entity;
        domaineServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(domaineServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.domaine = entity;
        domaineServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(domaineServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDomaine = { id: 123 };
        domaineServiceStub.find.resolves(foundDomaine);
        domaineServiceStub.retrieve.resolves([foundDomaine]);

        // WHEN
        comp.beforeRouteEnter({ params: { domaineId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.domaine).toBe(foundDomaine);
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
