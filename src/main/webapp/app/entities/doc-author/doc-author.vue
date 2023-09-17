<template>
  <div>
    <h2 id="page-heading" data-cy="DocAuthorHeading">
      <span id="doc-author-heading">Doc Authors</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DocAuthorCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-doc-author"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Doc Author </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && docAuthors && docAuthors.length === 0">
      <span>No docAuthors found</span>
    </div>
    <div class="table-responsive" v-if="docAuthors && docAuthors.length > 0">
      <table class="table table-striped" aria-describedby="docAuthors">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span>Name</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('imgUrl')">
              <span>Img Url</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'imgUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span>Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('url')">
              <span>Url</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'url'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="docAuthor in docAuthors" :key="docAuthor.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DocAuthorView', params: { docAuthorId: docAuthor.id } }">{{ docAuthor.id }}</router-link>
            </td>
            <td>{{ docAuthor.name }}</td>
            <td>{{ docAuthor.imgUrl }}</td>
            <td>{{ docAuthor.notes }}</td>
            <td>{{ docAuthor.url }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DocAuthorView', params: { docAuthorId: docAuthor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DocAuthorEdit', params: { docAuthorId: docAuthor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(docAuthor)"
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
        ><span id="mydocsApp.docAuthor.delete.question" data-cy="docAuthorDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-docAuthor-heading">Are you sure you want to delete this Doc Author?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-docAuthor"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDocAuthor()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="docAuthors && docAuthors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc-author.component.ts"></script>
