import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import { IDocBorrowed, DocBorrowed } from '@/shared/model/doc-borrowed.model';
import DocBorrowedService from './doc-borrowed.service';

const validations: any = {
  docBorrowed: {
    borrowDate: {},
    borrowerName: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class DocBorrowedUpdate extends Vue {
  @Inject('docBorrowedService') private docBorrowedService: () => DocBorrowedService;
  @Inject('alertService') private alertService: () => AlertService;

  public docBorrowed: IDocBorrowed = new DocBorrowed();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docBorrowedId) {
        vm.retrieveDocBorrowed(to.params.docBorrowedId);
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
    if (this.docBorrowed.id) {
      this.docBorrowedService()
        .update(this.docBorrowed)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocBorrowed is updated with identifier ' + param.id;
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
      this.docBorrowedService()
        .create(this.docBorrowed)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocBorrowed is created with identifier ' + param.id;
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

  public retrieveDocBorrowed(docBorrowedId): void {
    this.docBorrowedService()
      .find(docBorrowedId)
      .then(res => {
        this.docBorrowed = res;
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
