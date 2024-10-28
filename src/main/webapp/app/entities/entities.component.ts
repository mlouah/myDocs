import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import DocService from './doc/doc.service';
import DocPublisherService from './doc-publisher/doc-publisher.service';
import DocAuthorService from './doc-author/doc-author.service';
import DocTopicService from './doc-topic/doc-topic.service';
import DocBorrowedService from './doc-borrowed/doc-borrowed.service';
import DocFormatService from './doc-format/doc-format.service';
import DocCollectionService from './doc-collection/doc-collection.service';
import LanguageService from './language/language.service';
import DomaineService from './domaine/domaine.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('docService') private docService = () => new DocService();
  @Provide('docPublisherService') private docPublisherService = () => new DocPublisherService();
  @Provide('docAuthorService') private docAuthorService = () => new DocAuthorService();
  @Provide('docTopicService') private docTopicService = () => new DocTopicService();
  @Provide('docBorrowedService') private docBorrowedService = () => new DocBorrowedService();
  @Provide('docFormatService') private docFormatService = () => new DocFormatService();
  @Provide('docCollectionService') private docCollectionService = () => new DocCollectionService();
  @Provide('languageService') private languageService = () => new LanguageService();
  @Provide('domaineService') private domaineService = () => new DomaineService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
