<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.docTopic.home.createOrEditLabel" data-cy="DocTopicCreateUpdateHeading">Create or edit a DocTopic</h2>
        <div>
          <div class="form-group" v-if="docTopic.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docTopic.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-topic-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="doc-topic-name"
              data-cy="name"
              :class="{ valid: !$v.docTopic.name.$invalid, invalid: $v.docTopic.name.$invalid }"
              v-model="$v.docTopic.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-topic-code">Code</label>
            <input
              type="text"
              class="form-control"
              name="code"
              id="doc-topic-code"
              data-cy="code"
              :class="{ valid: !$v.docTopic.code.$invalid, invalid: $v.docTopic.code.$invalid }"
              v-model="$v.docTopic.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-topic-imgUrl">Img Url</label>
            <input
              type="text"
              class="form-control"
              name="imgUrl"
              id="doc-topic-imgUrl"
              data-cy="imgUrl"
              :class="{ valid: !$v.docTopic.imgUrl.$invalid, invalid: $v.docTopic.imgUrl.$invalid }"
              v-model="$v.docTopic.imgUrl.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-topic-notes">Notes</label>
            <textarea
              class="form-control"
              name="notes"
              id="doc-topic-notes"
              data-cy="notes"
              :class="{ valid: !$v.docTopic.notes.$invalid, invalid: $v.docTopic.notes.$invalid }"
              v-model="$v.docTopic.notes.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-topic-domaine">Domaine</label>
            <select class="form-control" id="doc-topic-domaine" data-cy="domaine" name="domaine" v-model="docTopic.domaine">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="docTopic.domaine && domaineOption.id === docTopic.domaine.id ? docTopic.domaine : domaineOption"
                v-for="domaineOption in domaines"
                :key="domaineOption.id"
              >
                {{ domaineOption.name }}
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
            :disabled="$v.docTopic.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-topic-update.component.ts"></script>
