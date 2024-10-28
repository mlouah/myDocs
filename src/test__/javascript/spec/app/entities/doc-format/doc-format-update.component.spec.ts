/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocFormatUpdateComponent from '@/entities/doc-format/doc-format-update.vue';
import DocFormatClass from '@/entities/doc-format/doc-format-update.component';
import DocFormatService from '@/entities/doc-format/doc-format.service';

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
  describe('DocFormat Management Update Component', () => {
    let wrapper: Wrapper<DocFormatClass>;
    let comp: DocFormatClass;
    let docFormatServiceStub: SinonStubbedInstance<DocFormatService>;

    beforeEach(() => {
      docFormatServiceStub = sinon.createStubInstance<DocFormatService>(DocFormatService);

      wrapper = shallowMount<DocFormatClass>(DocFormatUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docFormatService: () => docFormatServiceStub,
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
        comp.docFormat = entity;
        docFormatServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docFormatServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docFormat = entity;
        docFormatServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docFormatServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocFormat = { id: 123 };
        docFormatServiceStub.find.resolves(foundDocFormat);
        docFormatServiceStub.retrieve.resolves([foundDocFormat]);

        // WHEN
        comp.beforeRouteEnter({ params: { docFormatId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docFormat).toBe(foundDocFormat);
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
