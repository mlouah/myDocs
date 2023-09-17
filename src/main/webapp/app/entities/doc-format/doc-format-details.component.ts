import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDocFormat } from '@/shared/model/doc-format.model';
import DocFormatService from './doc-format.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocFormatDetails extends Vue {
  @Inject('docFormatService') private docFormatService: () => DocFormatService;
  @Inject('alertService') private alertService: () => AlertService;

  public docFormat: IDocFormat = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docFormatId) {
        vm.retrieveDocFormat(to.params.docFormatId);
      }
    });
  }

  public retrieveDocFormat(docFormatId) {
    this.docFormatService()
      .find(docFormatId)
      .then(res => {
        this.docFormat = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
