<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.domaine.home.createOrEditLabel" data-cy="DomaineCreateUpdateHeading">Create or edit a Domaine</h2>
        <div>
          <div class="form-group" v-if="domaine.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="domaine.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="domaine-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="domaine-name"
              data-cy="name"
              :class="{ valid: !$v.domaine.name.$invalid, invalid: $v.domaine.name.$invalid }"
              v-model="$v.domaine.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="domaine-code">Code</label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="domaine-code"
              data-cy="code"
              :class="{ valid: !$v.domaine.code.$invalid, invalid: $v.domaine.code.$invalid }"
              v-model="$v.domaine.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="domaine-notes">Notes</label>
            <textarea
              class="form-control"
              name="notes"
              id="domaine-notes"
              data-cy="notes"
              :class="{ valid: !$v.domaine.notes.$invalid, invalid: $v.domaine.notes.$invalid }"
              v-model="$v.domaine.notes.$model"
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
            :disabled="$v.domaine.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./domaine-update.component.ts"></script>
