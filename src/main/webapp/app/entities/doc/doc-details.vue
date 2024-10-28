<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="doc">
        <h2 class="jh-entity-heading" data-cy="docDetailsHeading"><span>Doc</span> {{ doc.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Title</span>
          </dt>
          <dd>
            <span>{{ doc.title }}</span>
          </dd>
          <dt>
            <span>Sub Title</span>
          </dt>
          <dd>
            <span>{{ doc.subTitle }}</span>
          </dd>
          <dt>
            <span>Publish Year</span>
          </dt>
          <dd>
            <span>{{ doc.publishYear }}</span>
          </dd>
          <dt>
            <span>Edition Numer</span>
          </dt>
          <dd>
            <span>{{ doc.editionNumer }}</span>
          </dd>
          <dt>
            <span>Purchase Date</span>
          </dt>
          <dd>
            <span>{{ doc.purchaseDate }}</span>
          </dd>
          <dt>
            <span>Start Reading Date</span>
          </dt>
          <dd>
            <span>{{ doc.startReadingDate }}</span>
          </dd>
          <dt>
            <span>End Reading Date</span>
          </dt>
          <dd>
            <span>{{ doc.endReadingDate }}</span>
          </dd>
          <dt>
            <span>Price</span>
          </dt>
          <dd>
            <span>{{ doc.price }}</span>
          </dd>
          <dt>
            <span>Rating</span>
          </dt>
          <dd>
            <span>{{ doc.rating }}</span>
          </dd>
          <dt>
            <span>Page Number</span>
          </dt>
          <dd>
            <span>{{ doc.pageNumber }}</span>
          </dd>
          <dt>
            <span>Num Doc</span>
          </dt>
          <dd>
            <span>{{ doc.numDoc }}</span>
          </dd>
          <dt>
            <span>Keywords</span>
          </dt>
          <dd>
            <span>{{ doc.keywords }}</span>
          </dd>
          <dt>
            <span>Toc</span>
          </dt>
          <dd>
            <span v-html="doc.toc"></span>
          </dd>
          <dt>
            <span>Filename</span>
          </dt>
          <dd>
            <span>{{ doc.filename }}</span>
          </dd>
          <dt>
            <span>Summary</span>
          </dt>
          <dd>
            <span v-html="doc.summary"></span>
          </dd>
          <dt>
            <span>Cover Img Path</span>
          </dt>
          <dd>
            <span>{{ doc.coverImgPath }}</span>
          </dd>
          <dt>
            <span>My Notes</span>
          </dt>
          <dd>
            <span>{{ doc.myNotes }}</span>
          </dd>
          <dt>
            <span>Publisher</span>
          </dt>
          <dd>
            <div v-if="doc.publisher">
              <router-link :to="{ name: 'DocPublisherView', params: { docPublisherId: doc.publisher.id } }">{{
                doc.publisher.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Format</span>
          </dt>
          <dd>
            <div v-if="doc.format">
              <router-link :to="{ name: 'DocFormatView', params: { docFormatId: doc.format.id } }">{{ doc.format.format }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Langue</span>
          </dt>
          <dd>
            <div v-if="doc.langue">
              <router-link :to="{ name: 'LanguageView', params: { languageId: doc.langue.id } }">{{ doc.langue.code }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Maintopic</span>
          </dt>
          <dd>
            <div v-if="doc.maintopic">
              <router-link :to="{ name: 'DocTopicView', params: { docTopicId: doc.maintopic.id } }">{{ doc.maintopic.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Main Author</span>
          </dt>
          <dd>
            <div v-if="doc.mainAuthor">
              <router-link :to="{ name: 'DocAuthorView', params: { docAuthorId: doc.mainAuthor.id } }">{{
                doc.mainAuthor.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Collection</span>
          </dt>
          <dd>
            <div v-if="doc.collection">
              <router-link :to="{ name: 'DocCollectionView', params: { docCollectionId: doc.collection.id } }">{{
                doc.collection.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="doc.id" :to="{ name: 'DocEdit', params: { docId: doc.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc-details.component.ts"></script>
