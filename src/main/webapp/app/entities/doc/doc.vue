<template>
  <div>
    <h2 id="page-heading" data-cy="DocHeading">
      <span id="doc-heading">Docs</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DocCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-doc">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Doc </span>
          </button>
        </router-link>
      </div>
    </h2>

    <br />
    <div class="alert alert-warning" v-if="!isFetching && docs && docs.length === 0">
      <span>No docs found</span>
    </div>

    <div class="table-responsive" v-if="docs && docs.length > 0">
      <table class="table table-striped" aria-describedby="docs">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span>Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('subTitle')">
              <span> </span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subTitle'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('publishYear')">
              <span>Publish Year</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'publishYear'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('editionNumer')">
              <span>Edition Numer</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'editionNumer'"></jhi-sort-indicator>
            </th>
            <!--
            <th scope="row" v-on:click="changeOrder('purchaseDate')">
              <span>Purchase Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'purchaseDate'"></jhi-sort-indicator>
            </th>
			-->
            <th scope="row" v-on:click="changeOrder('startReadingDate')">
              <span>Start Reading Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startReadingDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('endReadingDate')">
              <span>End Reading Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endReadingDate'"></jhi-sort-indicator>
            </th>
            <!--
            <th scope="row" v-on:click="changeOrder('price')">
              <span>Price</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'price'"></jhi-sort-indicator>
            </th>
			-->
            <!--
            <th scope="row" v-on:click="changeOrder('rating')">
              <span>Rating</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'rating'"></jhi-sort-indicator>
            </th>

            <th scope="row" v-on:click="changeOrder('pageNumber')">
              <span>Page Number</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'pageNumber'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('numDoc')">
              <span>Num Doc</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numDoc'"></jhi-sort-indicator>
            </th>
            -->
            <!--
			<th scope="row" v-on:click="changeOrder('keywords')">
              <span>Keywords</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'keywords'"></jhi-sort-indicator>
            </th>

            <th scope="row" v-on:click="changeOrder('toc')">
              <span>Toc</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toc'"></jhi-sort-indicator>
            </th>

            <th scope="row" v-on:click="changeOrder('filename')">
              <span>Filename</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'filename'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('summary')">
              <span>Summary</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'summary'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('coverImgPath')">
              <span>Cover Img Path</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coverImgPath'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('myNotes')">
              <span>My Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'myNotes'"></jhi-sort-indicator>
            </th>
            -->

            <th scope="row" v-on:click="changeOrder('publisher.name')">
              <span>Publisher</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'publisher.name'"></jhi-sort-indicator>
            </th>
            <!--
            <th scope="row" v-on:click="changeOrder('format.format')">
              <span>Format</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'format.format'"></jhi-sort-indicator>
            </th>

            <th scope="row" v-on:click="changeOrder('langue.code')">
              <span>Langue</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'langue.code'"></jhi-sort-indicator>
            </th>
			-->
            <th scope="row" v-on:click="changeOrder('maintopic.name')">
              <span>Maintopic</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maintopic.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('mainAuthor.name')">
              <span>Main Author</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mainAuthor.name'"></jhi-sort-indicator>
            </th>
            <!--
            <th scope="row" v-on:click="changeOrder('collection.name')">
              <span>Collection</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'collection.name'"></jhi-sort-indicator>
            </th>
			-->
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="doc in docs" :key="doc.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DocView', params: { docId: doc.id } }">{{ doc.id }}</router-link>
            </td>
            <td>
              <strong>
                <span style="color: red"> {{ doc.title }}</span>
              </strong>
            </td>
            <td>
              <em>{{ doc.subTitle }}</em>
            </td>
            <td>{{ doc.publishYear }}</td>
            <td>{{ doc.editionNumer }}</td>
            <!-- <td>{{ doc.purchaseDate }}</td> -->
            <td>{{ doc.startReadingDate }}</td>
            <td>{{ doc.endReadingDate }}</td>
            <!--
			<td>{{ doc.price }}</td>
            <td>{{ doc.rating }}</td>
            <td>{{ doc.pageNumber }}</td>
            <td>{{ doc.numDoc }}</td>
			<td>{{ doc.keywords }}</td>
            <td>{{ doc.toc }}</td>
            <td>{{ doc.filename }}</td>
            <td>{{ doc.summary }}</td>
            <td>{{ doc.coverImgPath }}</td>
            <td>{{ doc.myNotes }}</td>
			-->
            <td>
              <div v-if="doc.publisher">
                <router-link :to="{ name: 'DocPublisherView', params: { docPublisherId: doc.publisher.id } }">{{
                  doc.publisher.name
                }}</router-link>
              </div>
            </td>
            <!--
			<td>
              <div v-if="doc.format">
                <router-link :to="{ name: 'DocFormatView', params: { docFormatId: doc.format.id } }">{{ doc.format.format }}</router-link>
              </div>
            </td>

            <td>
              <div v-if="doc.langue">
                <router-link :to="{ name: 'LanguageView', params: { languageId: doc.langue.id } }">{{ doc.langue.code }}</router-link>
              </div>
            </td>
            -->
            <td>
              <div v-if="doc.maintopic">
                <router-link :to="{ name: 'DocTopicView', params: { docTopicId: doc.maintopic.id } }">{{ doc.maintopic.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="doc.mainAuthor">
                <router-link :to="{ name: 'DocAuthorView', params: { docAuthorId: doc.mainAuthor.id } }">{{
                  doc.mainAuthor.name
                }}</router-link>
              </div>
            </td>
            <!--
			<td>

              <div v-if="doc.collection">
                <router-link :to="{ name: 'DocCollectionView', params: { docCollectionId: doc.collection.id } }">{{
                  doc.collection.name
                }}</router-link>
              </div>
            </td>
			-->
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DocView', params: { docId: doc.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>

                <router-link :to="{ name: 'DocEdit', params: { docId: doc.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>

                <b-button
                  v-on:click="prepareRemove(doc)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="mydocsApp.doc.delete.question" data-cy="docDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-doc-heading">Are you sure you want to delete this Doc?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-doc"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDoc()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="docs && docs.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc.component.ts"></script>
