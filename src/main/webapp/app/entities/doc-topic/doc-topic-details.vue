<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="docTopic">
        <h2 class="jh-entity-heading" data-cy="docTopicDetailsHeading"><span>DocTopic</span> {{ docTopic.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ docTopic.name }}</span>
          </dd>
          <dt>
            <span>Code</span>
          </dt>
          <dd>
            <span>{{ docTopic.code }}</span>
          </dd>
          <dt>
            <span>Img Url</span>
          </dt>
          <dd>
            <span>{{ docTopic.imgUrl }}</span>
          </dd>
          <dt>
            <span>Notes</span>
          </dt>
          <dd>
            <span>{{ docTopic.notes }}</span>
          </dd>
          <dt>
            <span>Domaine</span>
          </dt>
          <dd>
            <div v-if="docTopic.domaine">
              <router-link :to="{ name: 'DomaineView', params: { domaineId: docTopic.domaine.id } }">{{
                docTopic.domaine.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="docTopic.id" :to="{ name: 'DocTopicEdit', params: { docTopicId: docTopic.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./doc-topic-details.component.ts"></script>
