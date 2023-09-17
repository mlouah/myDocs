<template>
  <div>
    <h2 id="page-heading" data-cy="DocCollectionHeading">
      <span id="doc-collection-heading">Doc Collections</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DocCollectionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-doc-collection"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Doc Collection </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && docCollections && docCollections.length === 0">
      <span>No docCollections found</span>
    </div>
    <div class="table-responsive" v-if="docCollections && docCollections.length > 0">
      <table class="table table-striped" aria-describedby="docCollections">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span>Name</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span>Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('docPublisher.name')">
              <span>Doc Publisher</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'docPublisher.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="docCollection in docCollections" :key="docCollection.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DocCollectionView', params: { docCollectionId: docCollection.id } }">{{
                docCollection.id
              }}</router-link>
            </td>
            <td>{{ docCollection.name }}</td>
            <td>{{ docCollection.notes }}</td>
            <td>
              <div v-if="docCollection.docPublisher">
                <router-link :to="{ name: 'DocPublisherView', params: { docPublisherId: docCollection.docPublisher.id } }">{{
                  docCollection.docPublisher.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'DocCollectionView', params: { docCollectionId: docCollection.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'DocCollectionEdit', params: { docCollectionId: docCollection.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(docCollection)"
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
        ><span id="mydocsApp.docCollection.delete.question" data-cy="docCollectionDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-docCollection-heading">Are you sure you want to delete this Doc Collection?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-docCollection"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDocCollection()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="docCollections && docCollections.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc-collection.component.ts"></script>
