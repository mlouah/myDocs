/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocBorrowedUpdateComponent from '@/entities/doc-borrowed/doc-borrowed-update.vue';
import DocBorrowedClass from '@/entities/doc-borrowed/doc-borrowed-update.component';
import DocBorrowedService from '@/entities/doc-borrowed/doc-borrowed.service';

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
  describe('DocBorrowed Management Update Component', () => {
    let wrapper: Wrapper<DocBorrowedClass>;
    let comp: DocBorrowedClass;
    let docBorrowedServiceStub: SinonStubbedInstance<DocBorrowedService>;

    beforeEach(() => {
      docBorrowedServiceStub = sinon.createStubInstance<DocBorrowedService>(DocBorrowedService);

      wrapper = shallowMount<DocBorrowedClass>(DocBorrowedUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docBorrowedService: () => docBorrowedServiceStub,
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
        comp.docBorrowed = entity;
        docBorrowedServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docBorrowedServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docBorrowed = entity;
        docBorrowedServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docBorrowedServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocBorrowed = { id: 123 };
        docBorrowedServiceStub.find.resolves(foundDocBorrowed);
        docBorrowedServiceStub.retrieve.resolves([foundDocBorrowed]);

        // WHEN
        comp.beforeRouteEnter({ params: { docBorrowedId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docBorrowed).toBe(foundDocBorrowed);
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
