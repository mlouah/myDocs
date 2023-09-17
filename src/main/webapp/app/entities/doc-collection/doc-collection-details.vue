<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="docCollection">
        <h2 class="jh-entity-heading" data-cy="docCollectionDetailsHeading"><span>DocCollection</span> {{ docCollection.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ docCollection.name }}</span>
          </dd>
          <dt>
            <span>Notes</span>
          </dt>
          <dd>
            <span>{{ docCollection.notes }}</span>
          </dd>
          <dt>
            <span>Doc Publisher</span>
          </dt>
          <dd>
            <div v-if="docCollection.docPublisher">
              <router-link :to="{ name: 'DocPublisherView', params: { docPublisherId: docCollection.docPublisher.id } }">{{
                docCollection.docPublisher.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="docCollection.id"
          :to="{ name: 'DocCollectionEdit', params: { docCollectionId: docCollection.id } }"
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

<script lang="ts" src="./doc-collection-details.component.ts"></script>
