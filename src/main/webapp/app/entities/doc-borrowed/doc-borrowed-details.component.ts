import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDocBorrowed } from '@/shared/model/doc-borrowed.model';
import DocBorrowedService from './doc-borrowed.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocBorrowedDetails extends Vue {
  @Inject('docBorrowedService') private docBorrowedService: () => DocBorrowedService;
  @Inject('alertService') private alertService: () => AlertService;

  public docBorrowed: IDocBorrowed = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docBorrowedId) {
        vm.retrieveDocBorrowed(to.params.docBorrowedId);
      }
    });
  }

  public retrieveDocBorrowed(docBorrowedId) {
    this.docBorrowedService()
      .find(docBorrowedId)
      .then(res => {
        this.docBorrowed = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
