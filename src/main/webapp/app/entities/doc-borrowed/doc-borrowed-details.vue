<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="docBorrowed">
        <h2 class="jh-entity-heading" data-cy="docBorrowedDetailsHeading"><span>DocBorrowed</span> {{ docBorrowed.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Borrow Date</span>
          </dt>
          <dd>
            <span>{{ docBorrowed.borrowDate }}</span>
          </dd>
          <dt>
            <span>Borrower Name</span>
          </dt>
          <dd>
            <span>{{ docBorrowed.borrowerName }}</span>
          </dd>
          <dt>
            <span>Notes</span>
          </dt>
          <dd>
            <span>{{ docBorrowed.notes }}</span>
          </dd>
          <dt>
            <span>Doc</span>
          </dt>
          <dd>
            <div v-if="docBorrowed.doc">
              <router-link :to="{ name: 'DocView', params: { docId: docBorrowed.doc.id } }">{{ docBorrowed.doc.title }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="docBorrowed.id"
          :to="{ name: 'DocBorrowedEdit', params: { docBorrowedId: docBorrowed.id } }"
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

<script lang="ts" src="./doc-borrowed-details.component.ts"></script>
