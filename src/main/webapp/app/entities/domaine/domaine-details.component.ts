import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IDomaine } from '@/shared/model/domaine.model';
import DomaineService from './domaine.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DomaineDetails extends mixins(JhiDataUtils) {
  @Inject('domaineService') private domaineService: () => DomaineService;
  @Inject('alertService') private alertService: () => AlertService;

  public domaine: IDomaine = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.domaineId) {
        vm.retrieveDomaine(to.params.domaineId);
      }
    });
  }

  public retrieveDomaine(domaineId) {
    this.domaineService()
      .find(domaineId)
      .then(res => {
        this.domaine = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
