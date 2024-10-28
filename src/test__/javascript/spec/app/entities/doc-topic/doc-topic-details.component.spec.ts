/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DocTopicDetailComponent from '@/entities/doc-topic/doc-topic-details.vue';
import DocTopicClass from '@/entities/doc-topic/doc-topic-details.component';
import DocTopicService from '@/entities/doc-topic/doc-topic.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DocTopic Management Detail Component', () => {
    let wrapper: Wrapper<DocTopicClass>;
    let comp: DocTopicClass;
    let docTopicServiceStub: SinonStubbedInstance<DocTopicService>;

    beforeEach(() => {
      docTopicServiceStub = sinon.createStubInstance<DocTopicService>(DocTopicService);

      wrapper = shallowMount<DocTopicClass>(DocTopicDetailComponent, {
        store,
        localVue,
        router,
        provide: { docTopicService: () => docTopicServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocTopic = { id: 123 };
        docTopicServiceStub.find.resolves(foundDocTopic);

        // WHEN
        comp.retrieveDocTopic(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docTopic).toBe(foundDocTopic);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocTopic = { id: 123 };
        docTopicServiceStub.find.resolves(foundDocTopic);

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
