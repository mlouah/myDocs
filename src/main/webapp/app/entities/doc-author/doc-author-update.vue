<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.docAuthor.home.createOrEditLabel" data-cy="DocAuthorCreateUpdateHeading">Create or edit a DocAuthor</h2>
        <div>
          <div class="form-group" v-if="docAuthor.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docAuthor.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-author-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="doc-author-name"
              data-cy="name"
              :class="{ valid: !$v.docAuthor.name.$invalid, invalid: $v.docAuthor.name.$invalid }"
              v-model="$v.docAuthor.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-author-imgUrl">Img Url</label>
            <input
              type="text"
              class="form-control"
              name="imgUrl"
              id="doc-author-imgUrl"
              data-cy="imgUrl"
              :class="{ valid: !$v.docAuthor.imgUrl.$invalid, invalid: $v.docAuthor.imgUrl.$invalid }"
              v-model="$v.docAuthor.imgUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-author-notes">Notes</label>
            <textarea
              class="form-control"
              name="notes"
              id="doc-author-notes"
              data-cy="notes"
              :class="{ valid: !$v.docAuthor.notes.$invalid, invalid: $v.docAuthor.notes.$invalid }"
              v-model="$v.docAuthor.notes.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-author-url">Url</label>
            <input
              type="text"
              class="form-control"
              name="url"
              id="doc-author-url"
              data-cy="url"
              :class="{ valid: !$v.docAuthor.url.$invalid, invalid: $v.docAuthor.url.$invalid }"
              v-model="$v.docAuthor.url.$model"
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
            :disabled="$v.docAuthor.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-author-update.component.ts"></script>
