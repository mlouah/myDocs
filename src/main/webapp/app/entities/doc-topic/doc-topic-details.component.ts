import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IDocTopic } from '@/shared/model/doc-topic.model';
import DocTopicService from './doc-topic.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocTopicDetails extends mixins(JhiDataUtils) {
  @Inject('docTopicService') private docTopicService: () => DocTopicService;
  @Inject('alertService') private alertService: () => AlertService;

  public docTopic: IDocTopic = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docTopicId) {
        vm.retrieveDocTopic(to.params.docTopicId);
      }
    });
  }

  public retrieveDocTopic(docTopicId) {
    this.docTopicService()
      .find(docTopicId)
      .then(res => {
        this.docTopic = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
