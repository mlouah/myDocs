/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocPublisherDetailComponent from '@/entities/doc-publisher/doc-publisher-details.vue';
import DocPublisherClass from '@/entities/doc-publisher/doc-publisher-details.component';
import DocPublisherService from '@/entities/doc-publisher/doc-publisher.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DocPublisher Management Detail Component', () => {
    let wrapper: Wrapper<DocPublisherClass>;
    let comp: DocPublisherClass;
    let docPublisherServiceStub: SinonStubbedInstance<DocPublisherService>;

    beforeEach(() => {
      docPublisherServiceStub = sinon.createStubInstance<DocPublisherService>(DocPublisherService);

      wrapper = shallowMount<DocPublisherClass>(DocPublisherDetailComponent, {
        store,
        localVue,
        router,
        provide: { docPublisherService: () => docPublisherServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocPublisher = { id: 123 };
        docPublisherServiceStub.find.resolves(foundDocPublisher);

        // WHEN
        comp.retrieveDocPublisher(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docPublisher).toBe(foundDocPublisher);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocPublisher = { id: 123 };
        docPublisherServiceStub.find.resolves(foundDocPublisher);

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
