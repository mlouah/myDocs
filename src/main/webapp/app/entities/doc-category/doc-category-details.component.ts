import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IDocCategory } from '@/shared/model/doc-category.model';
import DocCategoryService from './doc-category.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocCategoryDetails extends mixins(JhiDataUtils) {
  @Inject('docCategoryService') private docCategoryService: () => DocCategoryService;
  @Inject('alertService') private alertService: () => AlertService;

  public docCategory: IDocCategory = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docCategoryId) {
        vm.retrieveDocCategory(to.params.docCategoryId);
      }
    });
  }

  public retrieveDocCategory(docCategoryId) {
    this.docCategoryService()
      .find(docCategoryId)
      .then(res => {
        this.docCategory = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
