<template>
  <div>
    <h2 id="page-heading" data-cy="DocBorrowedHeading">
      <span id="doc-borrowed-heading">Doc Borroweds</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DocBorrowedCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-doc-borrowed"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Doc Borrowed </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && docBorroweds && docBorroweds.length === 0">
      <span>No docBorroweds found</span>
    </div>
    <div class="table-responsive" v-if="docBorroweds && docBorroweds.length > 0">
      <table class="table table-striped" aria-describedby="docBorroweds">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('borrowDate')">
              <span>Borrow Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'borrowDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('borrowerName')">
              <span>Borrower Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'borrowerName'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span>Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('doc.title')">
              <span>Doc</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'doc.title'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="docBorrowed in docBorroweds" :key="docBorrowed.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DocBorrowedView', params: { docBorrowedId: docBorrowed.id } }">{{ docBorrowed.id }}</router-link>
            </td>
            <td>{{ docBorrowed.borrowDate }}</td>
            <td>{{ docBorrowed.borrowerName }}</td>
            <td>{{ docBorrowed.notes }}</td>
            <td>
              <div v-if="docBorrowed.doc">
                <router-link :to="{ name: 'DocView', params: { docId: docBorrowed.doc.id } }">{{ docBorrowed.doc.title }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DocBorrowedView', params: { docBorrowedId: docBorrowed.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DocBorrowedEdit', params: { docBorrowedId: docBorrowed.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(docBorrowed)"
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
        ><span id="mydocsApp.docBorrowed.delete.question" data-cy="docBorrowedDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-docBorrowed-heading">Are you sure you want to delete this Doc Borrowed?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-docBorrowed"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDocBorrowed()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="docBorroweds && docBorroweds.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc-borrowed.component.ts"></script>
