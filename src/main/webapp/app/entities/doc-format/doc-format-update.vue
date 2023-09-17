<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.docFormat.home.createOrEditLabel" data-cy="DocFormatCreateUpdateHeading">Create or edit a DocFormat</h2>
        <div>
          <div class="form-group" v-if="docFormat.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docFormat.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-format-format">Format</label>
            <input
              type="text"
              class="form-control"
              name="format"
              id="doc-format-format"
              data-cy="format"
              :class="{ valid: !$v.docFormat.format.$invalid, invalid: $v.docFormat.format.$invalid }"
              v-model="$v.docFormat.format.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-format-code">Code</label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="doc-format-code"
              data-cy="code"
              :class="{ valid: !$v.docFormat.code.$invalid, invalid: $v.docFormat.code.$invalid }"
              v-model="$v.docFormat.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-format-notes">Notes</label>
            <input
              type="text"
              class="form-control"
              name="notes"
              id="doc-format-notes"
              data-cy="notes"
              :class="{ valid: !$v.docFormat.notes.$invalid, invalid: $v.docFormat.notes.$invalid }"
              v-model="$v.docFormat.notes.$model"
            />
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
            :disabled="$v.docFormat.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-format-update.component.ts"></script>
