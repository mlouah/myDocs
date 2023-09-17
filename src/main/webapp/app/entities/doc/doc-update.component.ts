import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { required, minLength, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import DocPublisherService from '@/entities/doc-publisher/doc-publisher.service';
import { IDocPublisher } from '@/shared/model/doc-publisher.model';

import DocCollectionService from '@/entities/doc-collection/doc-collection.service';
import { IDocCollection } from '@/shared/model/doc-collection.model';

import DocFormatService from '@/entities/doc-format/doc-format.service';
import { IDocFormat } from '@/shared/model/doc-format.model';

import LanguageService from '@/entities/language/language.service';
import { ILanguage } from '@/shared/model/language.model';

import DocTopicService from '@/entities/doc-topic/doc-topic.service';
import { IDocTopic } from '@/shared/model/doc-topic.model';

import DocAuthorService from '@/entities/doc-author/doc-author.service';
import { IDocAuthor } from '@/shared/model/doc-author.model';

import DocCategoryService from '@/entities/doc-category/doc-category.service';
import { IDocCategory } from '@/shared/model/doc-category.model';

import { IDoc, Doc } from '@/shared/model/doc.model';
import DocService from './doc.service';

const validations: any = {
  doc: {
    title: {
      required,
    },
    subTitle: {},
    publishYear: {
      minLength: minLength(4),
      maxLength: maxLength(4),
    },
    coverImgPath: {},
    editionNumer: {},
    summary: {},
    purchaseDate: {},
    startReadingDate: {},
    endReadingDate: {},
    price: {},
    copies: {},
    pageNumber: {},
    numDoc: {},
    myNotes: {},
    keywords: {},
    toc: {},
    filename: {},
  },
};

@Component({
  validations,
})
export default class DocUpdate extends mixins(JhiDataUtils) {
  @Inject('docService') private docService: () => DocService;
  @Inject('alertService') private alertService: () => AlertService;

  public doc: IDoc = new Doc();

  @Inject('docPublisherService') private docPublisherService: () => DocPublisherService;

  public docPublishers: IDocPublisher[] = [];

  @Inject('docCollectionService') private docCollectionService: () => DocCollectionService;

  public docCollections: IDocCollection[] = [];

  @Inject('docFormatService') private docFormatService: () => DocFormatService;

  public docFormats: IDocFormat[] = [];

  @Inject('languageService') private languageService: () => LanguageService;

  public languages: ILanguage[] = [];

  @Inject('docTopicService') private docTopicService: () => DocTopicService;

  public docTopics: IDocTopic[] = [];

  @Inject('docAuthorService') private docAuthorService: () => DocAuthorService;

  public docAuthors: IDocAuthor[] = [];

  @Inject('docCategoryService') private docCategoryService: () => DocCategoryService;

  public docCategories: IDocCategory[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.docId) {
        vm.retrieveDoc(to.params.docId);
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
    if (this.doc.id) {
      this.docService()
        .update(this.doc)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Doc is updated with identifier ' + param.id;
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
      this.docService()
        .create(this.doc)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Doc is created with identifier ' + param.id;
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

  public retrieveDoc(docId): void {
    this.docService()
      .find(docId)
      .then(res => {
        this.doc = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.docPublisherService()
      .retrieve()
      .then(res => {
        this.docPublishers = res.data;
      });
    this.docCollectionService()
      .retrieve()
      .then(res => {
        this.docCollections = res.data;
      });
    this.docFormatService()
      .retrieve()
      .then(res => {
        this.docFormats = res.data;
      });
    this.languageService()
      .retrieve()
      .then(res => {
        this.languages = res.data;
      });
    this.docTopicService()
      .retrieve()
      .then(res => {
        this.docTopics = res.data;
      });
    this.docAuthorService()
      .retrieve()
      .then(res => {
        this.docAuthors = res.data;
      });
    this.docCategoryService()
      .retrieve()
      .then(res => {
        this.docCategories = res.data;
      });
  }
}
