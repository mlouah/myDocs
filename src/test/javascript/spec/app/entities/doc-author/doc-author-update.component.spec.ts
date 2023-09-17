/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DocAuthorUpdateComponent from '@/entities/doc-author/doc-author-update.vue';
import DocAuthorClass from '@/entities/doc-author/doc-author-update.component';
import DocAuthorService from '@/entities/doc-author/doc-author.service';

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
  describe('DocAuthor Management Update Component', () => {
    let wrapper: Wrapper<DocAuthorClass>;
    let comp: DocAuthorClass;
    let docAuthorServiceStub: SinonStubbedInstance<DocAuthorService>;

    beforeEach(() => {
      docAuthorServiceStub = sinon.createStubInstance<DocAuthorService>(DocAuthorService);

      wrapper = shallowMount<DocAuthorClass>(DocAuthorUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          docAuthorService: () => docAuthorServiceStub,
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
        comp.docAuthor = entity;
        docAuthorServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docAuthorServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docAuthor = entity;
        docAuthorServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(docAuthorServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocAuthor = { id: 123 };
        docAuthorServiceStub.find.resolves(foundDocAuthor);
        docAuthorServiceStub.retrieve.resolves([foundDocAuthor]);

        // WHEN
        comp.beforeRouteEnter({ params: { docAuthorId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docAuthor).toBe(foundDocAuthor);
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
