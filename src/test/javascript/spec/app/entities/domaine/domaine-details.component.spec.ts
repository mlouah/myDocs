/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DomaineDetailComponent from '@/entities/domaine/domaine-details.vue';
import DomaineClass from '@/entities/domaine/domaine-details.component';
import DomaineService from '@/entities/domaine/domaine.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Domaine Management Detail Component', () => {
    let wrapper: Wrapper<DomaineClass>;
    let comp: DomaineClass;
    let domaineServiceStub: SinonStubbedInstance<DomaineService>;

    beforeEach(() => {
      domaineServiceStub = sinon.createStubInstance<DomaineService>(DomaineService);

      wrapper = shallowMount<DomaineClass>(DomaineDetailComponent, {
        store,
        localVue,
        router,
        provide: { domaineService: () => domaineServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDomaine = { id: 123 };
        domaineServiceStub.find.resolves(foundDomaine);

        // WHEN
        comp.retrieveDomaine(123);
        await comp.$nextTick();

        // THEN
        expect(comp.domaine).toBe(foundDomaine);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDomaine = { id: 123 };
        domaineServiceStub.find.resolves(foundDomaine);

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
