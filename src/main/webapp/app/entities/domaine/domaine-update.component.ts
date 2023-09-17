import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import AlertService from '@/shared/alert/alert.service';

import DocTopicService from '@/entities/doc-topic/doc-topic.service';
import { IDocTopic } from '@/shared/model/doc-topic.model';

import { IDomaine, Domaine } from '@/shared/model/domaine.model';
import DomaineService from './domaine.service';

const validations: any = {
  domaine: {
    name: {},
    code: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class DomaineUpdate extends mixins(JhiDataUtils) {
  @Inject('domaineService') private domaineService: () => DomaineService;
  @Inject('alertService') private alertService: () => AlertService;

  public domaine: IDomaine = new Domaine();

  @Inject('docTopicService') private docTopicService: () => DocTopicService;

  public docTopics: IDocTopic[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.domaineId) {
        vm.retrieveDomaine(to.params.domaineId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.domaine.id) {
      this.domaineService()
        .update(this.domaine)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Domaine is updated with identifier ' + param.id;
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.domaineService()
        .create(this.domaine)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Domaine is created with identifier ' + param.id;
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveDomaine(domaineId): void {
    this.domaineService()
      .find(domaineId)
      .then(res => {
        this.domaine = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.docTopicService()
      .retrieve()
      .then(res => {
        this.docTopics = res.data;
      });
  }
}
