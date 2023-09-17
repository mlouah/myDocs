import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IDoc } from '@/shared/model/doc.model';
import DocService from './doc.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocDetails extends mixins(JhiDataUtils) {
  @Inject('docService') private docService: () => DocService;
  @Inject('alertService') private alertService: () => AlertService;

  public doc: IDoc = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docId) {
        vm.retrieveDoc(to.params.docId);
      }
    });
  }

  public retrieveDoc(docId) {
    this.docService()
      .find(docId)
      .then(res => {
        this.doc = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
