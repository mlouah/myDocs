<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="docAuthor">
        <h2 class="jh-entity-heading" data-cy="docAuthorDetailsHeading"><span>DocAuthor</span> {{ docAuthor.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ docAuthor.name }}</span>
          </dd>
          <dt>
            <span>Img Url</span>
          </dt>
          <dd>
            <span>{{ docAuthor.imgUrl }}</span>
          </dd>
          <dt>
            <span>Notes</span>
          </dt>
          <dd>
            <span>{{ docAuthor.notes }}</span>
          </dd>
          <dt>
            <span>Url</span>
          </dt>
          <dd>
            <span>{{ docAuthor.url }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="docAuthor.id"
          :to="{ name: 'DocAuthorEdit', params: { docAuthorId: docAuthor.id } }"
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

<script lang="ts" src="./doc-author-details.component.ts"></script>
