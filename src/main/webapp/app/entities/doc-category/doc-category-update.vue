<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.docCategory.home.createOrEditLabel" data-cy="DocCategoryCreateUpdateHeading">Create or edit a DocCategory</h2>
        <div>
          <div class="form-group" v-if="docCategory.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docCategory.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-category-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="doc-category-name"
              data-cy="name"
              :class="{ valid: !$v.docCategory.name.$invalid, invalid: $v.docCategory.name.$invalid }"
              v-model="$v.docCategory.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-category-code">Code</label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="doc-category-code"
              data-cy="code"
              :class="{ valid: !$v.docCategory.code.$invalid, invalid: $v.docCategory.code.$invalid }"
              v-model="$v.docCategory.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-category-notes">Notes</label>
            <textarea
              class="form-control"
              name="notes"
              id="doc-category-notes"
              data-cy="notes"
              :class="{ valid: !$v.docCategory.notes.$invalid, invalid: $v.docCategory.notes.$invalid }"
              v-model="$v.docCategory.notes.$model"
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
            :disabled="$v.docCategory.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-category-update.component.ts"></script>
