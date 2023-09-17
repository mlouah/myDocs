<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.docPublisher.home.createOrEditLabel" data-cy="DocPublisherCreateUpdateHeading">Create or edit a DocPublisher</h2>
        <div>
          <div class="form-group" v-if="docPublisher.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docPublisher.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-publisher-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="doc-publisher-name"
              data-cy="name"
              :class="{ valid: !$v.docPublisher.name.$invalid, invalid: $v.docPublisher.name.$invalid }"
              v-model="$v.docPublisher.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-publisher-notes">Notes</label>
            <textarea
              class="form-control"
              name="notes"
              id="doc-publisher-notes"
              data-cy="notes"
              :class="{ valid: !$v.docPublisher.notes.$invalid, invalid: $v.docPublisher.notes.$invalid }"
              v-model="$v.docPublisher.notes.$model"
            ></textarea>
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
            :disabled="$v.docPublisher.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-publisher-update.component.ts"></script>
