import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IDocAuthor } from '@/shared/model/doc-author.model';
import DocAuthorService from './doc-author.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocAuthorDetails extends mixins(JhiDataUtils) {
  @Inject('docAuthorService') private docAuthorService: () => DocAuthorService;
  @Inject('alertService') private alertService: () => AlertService;

  public docAuthor: IDocAuthor = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docAuthorId) {
        vm.retrieveDocAuthor(to.params.docAuthorId);
      }
    });
  }

  public retrieveDocAuthor(docAuthorId) {
    this.docAuthorService()
      .find(docAuthorId)
      .then(res => {
        this.docAuthor = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
