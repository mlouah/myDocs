import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDocCollection } from '@/shared/model/doc-collection.model';
import DocCollectionService from './doc-collection.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DocCollectionDetails extends Vue {
  @Inject('docCollectionService') private docCollectionService: () => DocCollectionService;
  @Inject('alertService') private alertService: () => AlertService;

  public docCollection: IDocCollection = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docCollectionId) {
        vm.retrieveDocCollection(to.params.docCollectionId);
      }
    });
  }

  public retrieveDocCollection(docCollectionId) {
    this.docCollectionService()
      .find(docCollectionId)
      .then(res => {
        this.docCollection = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
