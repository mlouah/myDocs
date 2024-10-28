<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="mydocsApp.doc.home.createOrEditLabel" data-cy="DocCreateUpdateHeading">Create or edit a Doc</h2>
        <div>
          <div class="form-group" v-if="doc.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="doc.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="doc-title"
              data-cy="title"
              :class="{ valid: !$v.doc.title.$invalid, invalid: $v.doc.title.$invalid }"
              v-model="$v.doc.title.$model"
              required
            />
            <div v-if="$v.doc.title.$anyDirty && $v.doc.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.doc.title.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-subTitle">Sub Title</label>
            <input
              type="text"
              class="form-control"
              name="subTitle"
              id="doc-subTitle"
              data-cy="subTitle"
              :class="{ valid: !$v.doc.subTitle.$invalid, invalid: $v.doc.subTitle.$invalid }"
              v-model="$v.doc.subTitle.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-publishYear">Publish Year</label>
            <input
              type="text"
              class="form-control"
              name="publishYear"
              id="doc-publishYear"
              data-cy="publishYear"
              :class="{ valid: !$v.doc.publishYear.$invalid, invalid: $v.doc.publishYear.$invalid }"
              v-model="$v.doc.publishYear.$model"
            />
            <div v-if="$v.doc.publishYear.$anyDirty && $v.doc.publishYear.$invalid">
              <small class="form-text text-danger" v-if="!$v.doc.publishYear.minLength">
                This field is required to be at least 4 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.doc.publishYear.maxLength">
                This field cannot be longer than 4 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-editionNumer">Edition Numer</label>
            <input
              type="number"
              class="form-control"
              name="editionNumer"
              id="doc-editionNumer"
              data-cy="editionNumer"
              :class="{ valid: !$v.doc.editionNumer.$invalid, invalid: $v.doc.editionNumer.$invalid }"
              v-model.number="$v.doc.editionNumer.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-purchaseDate">Purchase Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="doc-purchaseDate"
                  v-model="$v.doc.purchaseDate.$model"
                  name="purchaseDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="doc-purchaseDate"
                data-cy="purchaseDate"
                type="text"
                class="form-control"
                name="purchaseDate"
                :class="{ valid: !$v.doc.purchaseDate.$invalid, invalid: $v.doc.purchaseDate.$invalid }"
                v-model="$v.doc.purchaseDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-startReadingDate">Start Reading Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="doc-startReadingDate"
                  v-model="$v.doc.startReadingDate.$model"
                  name="startReadingDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="doc-startReadingDate"
                data-cy="startReadingDate"
                type="text"
                class="form-control"
                name="startReadingDate"
                :class="{ valid: !$v.doc.startReadingDate.$invalid, invalid: $v.doc.startReadingDate.$invalid }"
                v-model="$v.doc.startReadingDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-endReadingDate">End Reading Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="doc-endReadingDate"
                  v-model="$v.doc.endReadingDate.$model"
                  name="endReadingDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="doc-endReadingDate"
                data-cy="endReadingDate"
                type="text"
                class="form-control"
                name="endReadingDate"
                :class="{ valid: !$v.doc.endReadingDate.$invalid, invalid: $v.doc.endReadingDate.$invalid }"
                v-model="$v.doc.endReadingDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-price">Price</label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="doc-price"
              data-cy="price"
              :class="{ valid: !$v.doc.price.$invalid, invalid: $v.doc.price.$invalid }"
              v-model.number="$v.doc.price.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-rating">Rating</label>
            <input
              type="text"
              class="form-control"
              name="rating"
              id="doc-rating"
              data-cy="rating"
              :class="{ valid: !$v.doc.rating.$invalid, invalid: $v.doc.rating.$invalid }"
              v-model="$v.doc.rating.$model"
            />
            <div v-if="$v.doc.rating.$anyDirty && $v.doc.rating.$invalid">
              <small class="form-text text-danger" v-if="!$v.doc.rating.maxLength"> This field cannot be longer than 5 characters. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-pageNumber">Page Number</label>
            <input
              type="number"
              class="form-control"
              name="pageNumber"
              id="doc-pageNumber"
              data-cy="pageNumber"
              :class="{ valid: !$v.doc.pageNumber.$invalid, invalid: $v.doc.pageNumber.$invalid }"
              v-model.number="$v.doc.pageNumber.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-numDoc">Num Doc</label>
            <input
              type="text"
              class="form-control"
              name="numDoc"
              id="doc-numDoc"
              data-cy="numDoc"
              :class="{ valid: !$v.doc.numDoc.$invalid, invalid: $v.doc.numDoc.$invalid }"
              v-model="$v.doc.numDoc.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-keywords">Keywords</label>
            <textarea
              class="form-control"
              name="keywords"
              id="doc-keywords"
              data-cy="keywords"
              :class="{ valid: !$v.doc.keywords.$invalid, invalid: $v.doc.keywords.$invalid }"
              v-model="$v.doc.keywords.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-toc">Toc</label>
            <textarea
              class="form-control"
              name="toc"
              id="doc-toc"
              data-cy="toc"
              :class="{ valid: !$v.doc.toc.$invalid, invalid: $v.doc.toc.$invalid }"
              v-model="$v.doc.toc.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-filename">Filename</label>
            <input
              type="text"
              class="form-control"
              name="filename"
              id="doc-filename"
              data-cy="filename"
              :class="{ valid: !$v.doc.filename.$invalid, invalid: $v.doc.filename.$invalid }"
              v-model="$v.doc.filename.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-summary">Summary</label>
            <textarea
              class="form-control"
              name="summary"
              id="doc-summary"
              data-cy="summary"
              :class="{ valid: !$v.doc.summary.$invalid, invalid: $v.doc.summary.$invalid }"
              v-model="$v.doc.summary.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-coverImgPath">Cover Img Path</label>
            <input
              type="text"
              class="form-control"
              name="coverImgPath"
              id="doc-coverImgPath"
              data-cy="coverImgPath"
              :class="{ valid: !$v.doc.coverImgPath.$invalid, invalid: $v.doc.coverImgPath.$invalid }"
              v-model="$v.doc.coverImgPath.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-myNotes">My Notes</label>
            <textarea
              class="form-control"
              name="myNotes"
              id="doc-myNotes"
              data-cy="myNotes"
              :class="{ valid: !$v.doc.myNotes.$invalid, invalid: $v.doc.myNotes.$invalid }"
              v-model="$v.doc.myNotes.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-publisher">Publisher</label>
            <select class="form-control" id="doc-publisher" data-cy="publisher" name="publisher" v-model="doc.publisher">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="doc.publisher && docPublisherOption.id === doc.publisher.id ? doc.publisher : docPublisherOption"
                v-for="docPublisherOption in docPublishers"
                :key="docPublisherOption.id"
              >
                {{ docPublisherOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-format">Format</label>
            <select class="form-control" id="doc-format" data-cy="format" name="format" v-model="doc.format">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="doc.format && docFormatOption.id === doc.format.id ? doc.format : docFormatOption"
                v-for="docFormatOption in docFormats"
                :key="docFormatOption.id"
              >
                {{ docFormatOption.format }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-langue">Langue</label>
            <select class="form-control" id="doc-langue" data-cy="langue" name="langue" v-model="doc.langue">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="doc.langue && languageOption.id === doc.langue.id ? doc.langue : languageOption"
                v-for="languageOption in languages"
                :key="languageOption.id"
              >
                {{ languageOption.code }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-maintopic">Maintopic</label>
            <select class="form-control" id="doc-maintopic" data-cy="maintopic" name="maintopic" v-model="doc.maintopic">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="doc.maintopic && docTopicOption.id === doc.maintopic.id ? doc.maintopic : docTopicOption"
                v-for="docTopicOption in docTopics"
                :key="docTopicOption.id"
              >
                {{ docTopicOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-mainAuthor">Main Author</label>
            <select class="form-control" id="doc-mainAuthor" data-cy="mainAuthor" name="mainAuthor" v-model="doc.mainAuthor">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="doc.mainAuthor && docAuthorOption.id === doc.mainAuthor.id ? doc.mainAuthor : docAuthorOption"
                v-for="docAuthorOption in docAuthors"
                :key="docAuthorOption.id"
              >
                {{ docAuthorOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="doc-collection">Collection</label>
            <select class="form-control" id="doc-collection" data-cy="collection" name="collection" v-model="doc.collection">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="doc.collection && docCollectionOption.id === doc.collection.id ? doc.collection : docCollectionOption"
                v-for="docCollectionOption in docCollections"
                :key="docCollectionOption.id"
              >
                {{ docCollectionOption.name }}
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
            :disabled="$v.doc.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./doc-update.component.ts"></script>
