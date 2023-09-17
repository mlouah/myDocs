<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="docFormat">
        <h2 class="jh-entity-heading" data-cy="docFormatDetailsHeading"><span>DocFormat</span> {{ docFormat.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Format</span>
          </dt>
          <dd>
            <span>{{ docFormat.format }}</span>
          </dd>
          <dt>
            <span>Code</span>
          </dt>
          <dd>
            <span>{{ docFormat.code }}</span>
          </dd>
          <dt>
            <span>Notes</span>
          </dt>
          <dd>
            <span>{{ docFormat.notes }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="docFormat.id"
          :to="{ name: 'DocFormatEdit', params: { docFormatId: docFormat.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc-format-details.component.ts"></script>
