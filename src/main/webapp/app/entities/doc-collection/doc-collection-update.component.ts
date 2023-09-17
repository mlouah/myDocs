import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import DocPublisherService from '@/entities/doc-publisher/doc-publisher.service';
import { IDocPublisher } from '@/shared/model/doc-publisher.model';

import { IDocCollection, DocCollection } from '@/shared/model/doc-collection.model';
import DocCollectionService from './doc-collection.service';

const validations: any = {
  docCollection: {
    name: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class DocCollectionUpdate extends Vue {
  @Inject('docCollectionService') private docCollectionService: () => DocCollectionService;
  @Inject('alertService') private alertService: () => AlertService;

  public docCollection: IDocCollection = new DocCollection();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];

  @Inject('docPublisherService') private docPublisherService: () => DocPublisherService;

  public docPublishers: IDocPublisher[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docCollectionId) {
        vm.retrieveDocCollection(to.params.docCollectionId);
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
    if (this.docCollection.id) {
      this.docCollectionService()
        .update(this.docCollection)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocCollection is updated with identifier ' + param.id;
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
      this.docCollectionService()
        .create(this.docCollection)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocCollection is created with identifier ' + param.id;
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

  public retrieveDocCollection(docCollectionId): void {
    this.docCollectionService()
      .find(docCollectionId)
      .then(res => {
        this.docCollection = res;
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
    this.docPublisherService()
      .retrieve()
      .then(res => {
        this.docPublishers = res.data;
      });
  }
}
