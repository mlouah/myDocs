import { Component, Vue, Inject } from 'vue-property-decorator';

import { maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DocService from '@/entities/doc/doc.service';
import { IDoc } from '@/shared/model/doc.model';

import { ILanguage, Language } from '@/shared/model/language.model';
import LanguageService from './language.service';

const validations: any = {
  language: {
    name: {},
    code: {
      maxLength: maxLength(4),
    },
  },
};

@Component({
  validations,
})
export default class LanguageUpdate extends Vue {
  @Inject('languageService') private languageService: () => LanguageService;
  @Inject('alertService') private alertService: () => AlertService;

  public language: ILanguage = new Language();

  @Inject('docService') private docService: () => DocService;

  public docs: IDoc[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.languageId) {
        vm.retrieveLanguage(to.params.languageId);
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
    if (this.language.id) {
      this.languageService()
        .update(this.language)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Language is updated with identifier ' + param.id;
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
      this.languageService()
        .create(this.language)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Language is created with identifier ' + param.id;
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

  public retrieveLanguage(languageId): void {
    this.languageService()
      .find(languageId)
      .then(res => {
        this.language = res;
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
