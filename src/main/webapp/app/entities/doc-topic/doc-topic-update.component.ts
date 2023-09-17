import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import DomaineService from '@/entities/domaine/domaine.service';
import { IDomaine } from '@/shared/model/domaine.model';

import { IDocTopic, DocTopic } from '@/shared/model/doc-topic.model';
import DocTopicService from './doc-topic.service';

const validations: any = {
  docTopic: {
    name: {},
    code: {},
    imgUrl: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class DocTopicUpdate extends mixins(JhiDataUtils) {
  @Inject('docTopicService') private docTopicService: () => DocTopicService;
  @Inject('alertService') private alertService: () => AlertService;

  public docTopic: IDocTopic = new DocTopic();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];

  @Inject('domaineService') private domaineService: () => DomaineService;

  public domaines: IDomaine[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docTopicId) {
        vm.retrieveDocTopic(to.params.docTopicId);
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
    if (this.docTopic.id) {
      this.docTopicService()
        .update(this.docTopic)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocTopic is updated with identifier ' + param.id;
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
      this.docTopicService()
        .create(this.docTopic)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocTopic is created with identifier ' + param.id;
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

  public retrieveDocTopic(docTopicId): void {
    this.docTopicService()
      .find(docTopicId)
      .then(res => {
        this.docTopic = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.docService()
      .retrieve()
      .then(res => {
        this.docs = res.data;
      });
    this.domaineService()
      .retrieve()
      .then(res => {
        this.domaines = res.data;
      });
  }
}
