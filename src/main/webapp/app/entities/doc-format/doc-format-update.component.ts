import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import { IDocFormat, DocFormat } from '@/shared/model/doc-format.model';
import DocFormatService from './doc-format.service';

const validations: any = {
  docFormat: {
    format: {},
    code: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class DocFormatUpdate extends Vue {
  @Inject('docFormatService') private docFormatService: () => DocFormatService;
  @Inject('alertService') private alertService: () => AlertService;

  public docFormat: IDocFormat = new DocFormat();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docFormatId) {
        vm.retrieveDocFormat(to.params.docFormatId);
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
    if (this.docFormat.id) {
      this.docFormatService()
        .update(this.docFormat)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocFormat is updated with identifier ' + param.id;
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
      this.docFormatService()
        .create(this.docFormat)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A DocFormat is created with identifier ' + param.id;
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

  public retrieveDocFormat(docFormatId): void {
    this.docFormatService()
      .find(docFormatId)
      .then(res => {
        this.docFormat = res;
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
