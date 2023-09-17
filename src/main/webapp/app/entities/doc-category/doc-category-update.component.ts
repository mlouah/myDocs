import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import { IDocCategory, DocCategory } from '@/shared/model/doc-category.model';
import DocCategoryService from './doc-category.service';

const validations: any = {
  docCategory: {
    name: {},
    code: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class DocCategoryUpdate extends mixins(JhiDataUtils) {
  @Inject('docCategoryService') private docCategoryService: () => DocCategoryService;
  @Inject('alertService') private alertService: () => AlertService;

  public docCategory: IDocCategory = new DocCategory();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docCategoryId) {
        vm.retrieveDocCategory(to.params.docCategoryId);
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
    if (this.docCategory.id) {
      this.docCategoryService()
        .update(this.docCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocCategory is updated with identifier ' + param.id;
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
      this.docCategoryService()
        .create(this.docCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocCategory is created with identifier ' + param.id;
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

  public retrieveDocCategory(docCategoryId): void {
    this.docCategoryService()
      .find(docCategoryId)
      .then(res => {
        this.docCategory = res;
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
