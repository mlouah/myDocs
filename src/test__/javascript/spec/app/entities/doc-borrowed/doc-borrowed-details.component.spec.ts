/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocBorrowedDetailComponent from '@/entities/doc-borrowed/doc-borrowed-details.vue';
import DocBorrowedClass from '@/entities/doc-borrowed/doc-borrowed-details.component';
import DocBorrowedService from '@/entities/doc-borrowed/doc-borrowed.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DocBorrowed Management Detail Component', () => {
    let wrapper: Wrapper<DocBorrowedClass>;
    let comp: DocBorrowedClass;
    let docBorrowedServiceStub: SinonStubbedInstance<DocBorrowedService>;

    beforeEach(() => {
      docBorrowedServiceStub = sinon.createStubInstance<DocBorrowedService>(DocBorrowedService);

      wrapper = shallowMount<DocBorrowedClass>(DocBorrowedDetailComponent, {
        store,
        localVue,
        router,
        provide: { docBorrowedService: () => docBorrowedServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocBorrowed = { id: 123 };
        docBorrowedServiceStub.find.resolves(foundDocBorrowed);

        // WHEN
        comp.retrieveDocBorrowed(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docBorrowed).toBe(foundDocBorrowed);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocBorrowed = { id: 123 };
        docBorrowedServiceStub.find.resolves(foundDocBorrowed);

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
