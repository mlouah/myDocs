/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocAuthorDetailComponent from '@/entities/doc-author/doc-author-details.vue';
import DocAuthorClass from '@/entities/doc-author/doc-author-details.component';
import DocAuthorService from '@/entities/doc-author/doc-author.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DocAuthor Management Detail Component', () => {
    let wrapper: Wrapper<DocAuthorClass>;
    let comp: DocAuthorClass;
    let docAuthorServiceStub: SinonStubbedInstance<DocAuthorService>;

    beforeEach(() => {
      docAuthorServiceStub = sinon.createStubInstance<DocAuthorService>(DocAuthorService);

      wrapper = shallowMount<DocAuthorClass>(DocAuthorDetailComponent, {
        store,
        localVue,
        router,
        provide: { docAuthorService: () => docAuthorServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocAuthor = { id: 123 };
        docAuthorServiceStub.find.resolves(foundDocAuthor);

        // WHEN
        comp.retrieveDocAuthor(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docAuthor).toBe(foundDocAuthor);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocAuthor = { id: 123 };
        docAuthorServiceStub.find.resolves(foundDocAuthor);

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
