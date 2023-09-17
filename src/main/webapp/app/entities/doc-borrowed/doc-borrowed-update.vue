<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.docBorrowed.home.createOrEditLabel" data-cy="DocBorrowedCreateUpdateHeading">Create or edit a DocBorrowed</h2>
        <div>
          <div class="form-group" v-if="docBorrowed.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docBorrowed.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-borrowed-borrowDate">Borrow Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="doc-borrowed-borrowDate"
                  v-model="$v.docBorrowed.borrowDate.$model"
                  name="borrowDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="doc-borrowed-borrowDate"
                data-cy="borrowDate"
                type="text"
                class="form-control"
                name="borrowDate"
                :class="{ valid: !$v.docBorrowed.borrowDate.$invalid, invalid: $v.docBorrowed.borrowDate.$invalid }"
                v-model="$v.docBorrowed.borrowDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-borrowed-borrowerName">Borrower Name</label>
            <input
              type="text"
              class="form-control"
              name="borrowerName"
              id="doc-borrowed-borrowerName"
              data-cy="borrowerName"
              :class="{ valid: !$v.docBorrowed.borrowerName.$invalid, invalid: $v.docBorrowed.borrowerName.$invalid }"
              v-model="$v.docBorrowed.borrowerName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-borrowed-notes">Notes</label>
            <input
              type="text"
              class="form-control"
              name="notes"
              id="doc-borrowed-notes"
              data-cy="notes"
              :class="{ valid: !$v.docBorrowed.notes.$invalid, invalid: $v.docBorrowed.notes.$invalid }"
              v-model="$v.docBorrowed.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-borrowed-doc">Doc</label>
            <select class="form-control" id="doc-borrowed-doc" data-cy="doc" name="doc" v-model="docBorrowed.doc">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="docBorrowed.doc && docOption.id === docBorrowed.doc.id ? docBorrowed.doc : docOption"
                v-for="docOption in docs"
                :key="docOption.id"
              >
                {{ docOption.title }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.docBorrowed.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-borrowed-update.component.ts"></script>
