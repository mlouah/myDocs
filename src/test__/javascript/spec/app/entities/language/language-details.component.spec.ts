/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import LanguageDetailComponent from '@/entities/language/language-details.vue';
import LanguageClass from '@/entities/language/language-details.component';
import LanguageService from '@/entities/language/language.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Language Management Detail Component', () => {
    let wrapper: Wrapper<LanguageClass>;
    let comp: LanguageClass;
    let languageServiceStub: SinonStubbedInstance<LanguageService>;

    beforeEach(() => {
      languageServiceStub = sinon.createStubInstance<LanguageService>(LanguageService);

      wrapper = shallowMount<LanguageClass>(LanguageDetailComponent, {
        store,
        localVue,
        router,
        provide: { languageService: () => languageServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundLanguage = { id: 123 };
        languageServiceStub.find.resolves(foundLanguage);

        // WHEN
        comp.retrieveLanguage(123);
        await comp.$nextTick();

        // THEN
        expect(comp.language).toBe(foundLanguage);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLanguage = { id: 123 };
        languageServiceStub.find.resolves(foundLanguage);

        // WHEN
        comp.beforeRouteEnter({ params: { languageId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.language).toBe(foundLanguage);
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
