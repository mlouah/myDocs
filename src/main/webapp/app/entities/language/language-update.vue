<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.language.home.createOrEditLabel" data-cy="LanguageCreateUpdateHeading">Create or edit a Language</h2>
        <div>
          <div class="form-group" v-if="language.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="language.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="language-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="language-name"
              data-cy="name"
              :class="{ valid: !$v.language.name.$invalid, invalid: $v.language.name.$invalid }"
              v-model="$v.language.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="language-code">Code</label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="language-code"
              data-cy="code"
              :class="{ valid: !$v.language.code.$invalid, invalid: $v.language.code.$invalid }"
              v-model="$v.language.code.$model"
            />
            <div v-if="$v.language.code.$anyDirty && $v.language.code.$invalid">
              <small class="form-text text-danger" v-if="!$v.language.code.maxLength">
                This field cannot be longer than 4 characters.
              </small>
            </div>
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
            :disabled="$v.language.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./language-update.component.ts"></script>
