import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import { IDocAuthor, DocAuthor } from '@/shared/model/doc-author.model';
import DocAuthorService from './doc-author.service';

const validations: any = {
  docAuthor: {
    name: {},
    imgUrl: {},
    notes: {},
    url: {},
  },
};

@Component({
  validations,
})
export default class DocAuthorUpdate extends mixins(JhiDataUtils) {
  @Inject('docAuthorService') private docAuthorService: () => DocAuthorService;
  @Inject('alertService') private alertService: () => AlertService;

  public docAuthor: IDocAuthor = new DocAuthor();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docAuthorId) {
        vm.retrieveDocAuthor(to.params.docAuthorId);
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
    if (this.docAuthor.id) {
      this.docAuthorService()
        .update(this.docAuthor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocAuthor is updated with identifier ' + param.id;
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
      this.docAuthorService()
        .create(this.docAuthor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocAuthor is created with identifier ' + param.id;
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

  public retrieveDocAuthor(docAuthorId): void {
    this.docAuthorService()
      .find(docAuthorId)
      .then(res => {
        this.docAuthor = res;
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
  }
}
