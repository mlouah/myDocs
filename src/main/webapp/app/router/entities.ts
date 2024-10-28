import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Doc = () => import('@/entities/doc/doc.vue');
// prettier-ignore
const DocUpdate = () => import('@/entities/doc/doc-update.vue');
// prettier-ignore
const DocDetails = () => import('@/entities/doc/doc-details.vue');
// prettier-ignore
const DocPublisher = () => import('@/entities/doc-publisher/doc-publisher.vue');
// prettier-ignore
const DocPublisherUpdate = () => import('@/entities/doc-publisher/doc-publisher-update.vue');
// prettier-ignore
const DocPublisherDetails = () => import('@/entities/doc-publisher/doc-publisher-details.vue');
// prettier-ignore
const DocAuthor = () => import('@/entities/doc-author/doc-author.vue');
// prettier-ignore
const DocAuthorUpdate = () => import('@/entities/doc-author/doc-author-update.vue');
// prettier-ignore
const DocAuthorDetails = () => import('@/entities/doc-author/doc-author-details.vue');
// prettier-ignore
const DocTopic = () => import('@/entities/doc-topic/doc-topic.vue');
// prettier-ignore
const DocTopicUpdate = () => import('@/entities/doc-topic/doc-topic-update.vue');
// prettier-ignore
const DocTopicDetails = () => import('@/entities/doc-topic/doc-topic-details.vue');
// prettier-ignore
const DocBorrowed = () => import('@/entities/doc-borrowed/doc-borrowed.vue');
// prettier-ignore
const DocBorrowedUpdate = () => import('@/entities/doc-borrowed/doc-borrowed-update.vue');
// prettier-ignore
const DocBorrowedDetails = () => import('@/entities/doc-borrowed/doc-borrowed-details.vue');
// prettier-ignore
const DocFormat = () => import('@/entities/doc-format/doc-format.vue');
// prettier-ignore
const DocFormatUpdate = () => import('@/entities/doc-format/doc-format-update.vue');
// prettier-ignore
const DocFormatDetails = () => import('@/entities/doc-format/doc-format-details.vue');
// prettier-ignore
const DocCollection = () => import('@/entities/doc-collection/doc-collection.vue');
// prettier-ignore
const DocCollectionUpdate = () => import('@/entities/doc-collection/doc-collection-update.vue');
// prettier-ignore
const DocCollectionDetails = () => import('@/entities/doc-collection/doc-collection-details.vue');
// prettier-ignore
const Language = () => import('@/entities/language/language.vue');
// prettier-ignore
const LanguageUpdate = () => import('@/entities/language/language-update.vue');
// prettier-ignore
const LanguageDetails = () => import('@/entities/language/language-details.vue');
// prettier-ignore
const Domaine = () => import('@/entities/domaine/domaine.vue');
// prettier-ignore
const DomaineUpdate = () => import('@/entities/domaine/domaine-update.vue');
// prettier-ignore
const DomaineDetails = () => import('@/entities/domaine/domaine-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'doc',
      name: 'Doc',
      component: Doc,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc/new',
      name: 'DocCreate',
      component: DocUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc/:docId/edit',
      name: 'DocEdit',
      component: DocUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc/:docId/view',
      name: 'DocView',
      component: DocDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-publisher',
      name: 'DocPublisher',
      component: DocPublisher,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-publisher/new',
      name: 'DocPublisherCreate',
      component: DocPublisherUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-publisher/:docPublisherId/edit',
      name: 'DocPublisherEdit',
      component: DocPublisherUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-publisher/:docPublisherId/view',
      name: 'DocPublisherView',
      component: DocPublisherDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-author',
      name: 'DocAuthor',
      component: DocAuthor,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-author/new',
      name: 'DocAuthorCreate',
      component: DocAuthorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-author/:docAuthorId/edit',
      name: 'DocAuthorEdit',
      component: DocAuthorUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-author/:docAuthorId/view',
      name: 'DocAuthorView',
      component: DocAuthorDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-topic',
      name: 'DocTopic',
      component: DocTopic,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-topic/new',
      name: 'DocTopicCreate',
      component: DocTopicUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-topic/:docTopicId/edit',
      name: 'DocTopicEdit',
      component: DocTopicUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-topic/:docTopicId/view',
      name: 'DocTopicView',
      component: DocTopicDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-borrowed',
      name: 'DocBorrowed',
      component: DocBorrowed,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-borrowed/new',
      name: 'DocBorrowedCreate',
      component: DocBorrowedUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-borrowed/:docBorrowedId/edit',
      name: 'DocBorrowedEdit',
      component: DocBorrowedUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-borrowed/:docBorrowedId/view',
      name: 'DocBorrowedView',
      component: DocBorrowedDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-format',
      name: 'DocFormat',
      component: DocFormat,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-format/new',
      name: 'DocFormatCreate',
      component: DocFormatUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-format/:docFormatId/edit',
      name: 'DocFormatEdit',
      component: DocFormatUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-format/:docFormatId/view',
      name: 'DocFormatView',
      component: DocFormatDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-collection',
      name: 'DocCollection',
      component: DocCollection,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-collection/new',
      name: 'DocCollectionCreate',
      component: DocCollectionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-collection/:docCollectionId/edit',
      name: 'DocCollectionEdit',
      component: DocCollectionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'doc-collection/:docCollectionId/view',
      name: 'DocCollectionView',
      component: DocCollectionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language',
      name: 'Language',
      component: Language,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language/new',
      name: 'LanguageCreate',
      component: LanguageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language/:languageId/edit',
      name: 'LanguageEdit',
      component: LanguageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language/:languageId/view',
      name: 'LanguageView',
      component: LanguageDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'domaine',
      name: 'Domaine',
      component: Domaine,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'domaine/new',
      name: 'DomaineCreate',
      component: DomaineUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'domaine/:domaineId/edit',
      name: 'DomaineEdit',
      component: DomaineUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'domaine/:domaineId/view',
      name: 'DomaineView',
      component: DomaineDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
