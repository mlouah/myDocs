<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.docCollection.home.createOrEditLabel" data-cy="DocCollectionCreateUpdateHeading">
          Create or edit a DocCollection
        </h2>
        <div>
          <div class="form-group" v-if="docCollection.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docCollection.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-collection-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="doc-collection-name"
              data-cy="name"
              :class="{ valid: !$v.docCollection.name.$invalid, invalid: $v.docCollection.name.$invalid }"
              v-model="$v.docCollection.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-collection-notes">Notes</label>
            <input
              type="text"
              class="form-control"
              name="notes"
              id="doc-collection-notes"
              data-cy="notes"
              :class="{ valid: !$v.docCollection.notes.$invalid, invalid: $v.docCollection.notes.$invalid }"
              v-model="$v.docCollection.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-collection-docPublisher">Doc Publisher</label>
            <select
              class="form-control"
              id="doc-collection-docPublisher"
              data-cy="docPublisher"
              name="docPublisher"
              v-model="docCollection.docPublisher"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  docCollection.docPublisher && docPublisherOption.id === docCollection.docPublisher.id
                    ? docCollection.docPublisher
                    : docPublisherOption
                "
                v-for="docPublisherOption in docPublishers"
                :key="docPublisherOption.id"
              >
                {{ docPublisherOption.name }}
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
            :disabled="$v.docCollection.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-collection-update.component.ts"></script>
