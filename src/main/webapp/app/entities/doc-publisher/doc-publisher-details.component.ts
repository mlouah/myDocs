import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IDocPublisher } from '@/shared/model/doc-publisher.model';
import DocPublisherService from './doc-publisher.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocPublisherDetails extends mixins(JhiDataUtils) {
  @Inject('docPublisherService') private docPublisherService: () => DocPublisherService;
  @Inject('alertService') private alertService: () => AlertService;

  public docPublisher: IDocPublisher = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docPublisherId) {
        vm.retrieveDocPublisher(to.params.docPublisherId);
      }
    });
  }

  public retrieveDocPublisher(docPublisherId) {
    this.docPublisherService()
      .find(docPublisherId)
      .then(res => {
        this.docPublisher = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
