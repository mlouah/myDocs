<template>
  <div>
    <h2 id="page-heading" data-cy="DocTopicHeading">
      <span id="doc-topic-heading">Doc Topics</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DocTopicCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-doc-topic"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Doc Topic </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && docTopics && docTopics.length === 0">
      <span>No docTopics found</span>
    </div>
    <div class="table-responsive" v-if="docTopics && docTopics.length > 0">
      <table class="table table-striped" aria-describedby="docTopics">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span>Name</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('code')">
              <span>Code</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'code'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('imgUrl')">
              <span>Img Url</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'imgUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span>Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('domaine.name')">
              <span>Domaine</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'domaine.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="docTopic in docTopics" :key="docTopic.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DocTopicView', params: { docTopicId: docTopic.id } }">{{ docTopic.id }}</router-link>
            </td>
            <td>{{ docTopic.name }}</td>
            <td>{{ docTopic.code }}</td>
            <td>{{ docTopic.imgUrl }}</td>
            <td>{{ docTopic.notes }}</td>
            <td>
              <div v-if="docTopic.domaine">
                <router-link :to="{ name: 'DomaineView', params: { domaineId: docTopic.domaine.id } }">{{
                  docTopic.domaine.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DocTopicView', params: { docTopicId: docTopic.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DocTopicEdit', params: { docTopicId: docTopic.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(docTopic)"
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
        ><span id="mydocsApp.docTopic.delete.question" data-cy="docTopicDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-docTopic-heading">Are you sure you want to delete this Doc Topic?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-docTopic"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDocTopic()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="docTopics && docTopics.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc-topic.component.ts"></script>
