import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import DocCollectionService from '@/entities/doc-collection/doc-collection.service';
import { IDocCollection } from '@/shared/model/doc-collection.model';

import { IDocPublisher, DocPublisher } from '@/shared/model/doc-publisher.model';
import DocPublisherService from './doc-publisher.service';

const validations: any = {
  docPublisher: {
    name: {},
    notes: {},
    url: {},
  },
};

@Component({
  validations,
})
export default class DocPublisherUpdate extends mixins(JhiDataUtils) {
  @Inject('docPublisherService') private docPublisherService: () => DocPublisherService;
  @Inject('alertService') private alertService: () => AlertService;

  public docPublisher: IDocPublisher = new DocPublisher();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];

  @Inject('docCollectionService') private docCollectionService: () => DocCollectionService;

  public docCollections: IDocCollection[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docPublisherId) {
        vm.retrieveDocPublisher(to.params.docPublisherId);
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
    if (this.docPublisher.id) {
      this.docPublisherService()
        .update(this.docPublisher)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocPublisher is updated with identifier ' + param.id;
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
      this.docPublisherService()
        .create(this.docPublisher)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocPublisher is created with identifier ' + param.id;
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

  public retrieveDocPublisher(docPublisherId): void {
    this.docPublisherService()
      .find(docPublisherId)
      .then(res => {
        this.docPublisher = res;
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
    this.docCollectionService()
      .retrieve()
      .then(res => {
        this.docCollections = res.data;
      });
  }
}
