import { Component, Vue, Inject } from 'vue-property-decorator';

import { ILanguage } from '@/shared/model/language.model';
import LanguageService from './language.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class LanguageDetails extends Vue {
  @Inject('languageService') private languageService: () => LanguageService;
  @Inject('alertService') private alertService: () => AlertService;

  public language: ILanguage = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.languageId) {
        vm.retrieveLanguage(to.params.languageId);
      }
    });
  }

  public retrieveLanguage(languageId) {
    this.languageService()
      .find(languageId)
      .then(res => {
        this.language = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
