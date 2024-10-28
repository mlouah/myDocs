package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.DocCollection;
import com.perso.mydocs.domain.DocPublisher;
import com.perso.mydocs.repository.DocPublisherRepository;
import com.perso.mydocs.service.criteria.DocPublisherCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocPublisherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocPublisherResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-publishers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocPublisherRepository docPublisherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocPublisherMockMvc;

    private DocPublisher docPublisher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocPublisher createEntity(EntityManager em) {
        DocPublisher docPublisher = new DocPublisher().name(DEFAULT_NAME).notes(DEFAULT_NOTES);
        return docPublisher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocPublisher createUpdatedEntity(EntityManager em) {
        DocPublisher docPublisher = new DocPublisher().name(UPDATED_NAME).notes(UPDATED_NOTES);
        return docPublisher;
    }

    @BeforeEach
    public void initTest() {
        docPublisher = createEntity(em);
    }

    @Test
    @Transactional
    void createDocPublisher() throws Exception {
        int databaseSizeBeforeCreate = docPublisherRepository.findAll().size();
        // Create the DocPublisher
        restDocPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docPublisher)))
            .andExpect(status().isCreated());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeCreate + 1);
        DocPublisher testDocPublisher = docPublisherList.get(docPublisherList.size() - 1);
        assertThat(testDocPublisher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocPublisher.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createDocPublisherWithExistingId() throws Exception {
        // Create the DocPublisher with an existing ID
        docPublisher.setId(1L);

        int databaseSizeBeforeCreate = docPublisherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocPublisherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docPublisher)))
            .andExpect(status().isBadRequest());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocPublishers() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        // Get all the docPublisherList
        restDocPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docPublisher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    void getDocPublisher() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        // Get the docPublisher
        restDocPublisherMockMvc
            .perform(get(ENTITY_API_URL_ID, docPublisher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docPublisher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    void getDocPublishersByIdFiltering() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        Long id = docPublisher.getId();

        defaultDocPublisherShouldBeFound("id.equals=" + id);
        defaultDocPublisherShouldNotBeFound("id.notEquals=" + id);

        defaultDocPublisherShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocPublisherShouldNotBeFound("id.greaterThan=" + id);

        defaultDocPublisherShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocPublisherShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocPublishersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        // Get all the docPublisherList where name equals to DEFAULT_NAME
        defaultDocPublisherShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the docPublisherList where name equals to UPDATED_NAME
        defaultDocPublisherShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocPublishersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        // Get all the docPublisherList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocPublisherShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the docPublisherList where name equals to UPDATED_NAME
        defaultDocPublisherShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocPublishersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        // Get all the docPublisherList where name is not null
        defaultDocPublisherShouldBeFound("name.specified=true");

        // Get all the docPublisherList where name is null
        defaultDocPublisherShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocPublishersByNameContainsSomething() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        // Get all the docPublisherList where name contains DEFAULT_NAME
        defaultDocPublisherShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the docPublisherList where name contains UPDATED_NAME
        defaultDocPublisherShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocPublishersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        // Get all the docPublisherList where name does not contain DEFAULT_NAME
        defaultDocPublisherShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the docPublisherList where name does not contain UPDATED_NAME
        defaultDocPublisherShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocPublishersByCollectionIsEqualToSomething() throws Exception {
        DocCollection collection;
        if (TestUtil.findAll(em, DocCollection.class).isEmpty()) {
            docPublisherRepository.saveAndFlush(docPublisher);
            collection = DocCollectionResourceIT.createEntity(em);
        } else {
            collection = TestUtil.findAll(em, DocCollection.class).get(0);
        }
        em.persist(collection);
        em.flush();
        docPublisher.addCollection(collection);
        docPublisherRepository.saveAndFlush(docPublisher);
        Long collectionId = collection.getId();

        // Get all the docPublisherList where collection equals to collectionId
        defaultDocPublisherShouldBeFound("collectionId.equals=" + collectionId);

        // Get all the docPublisherList where collection equals to (collectionId + 1)
        defaultDocPublisherShouldNotBeFound("collectionId.equals=" + (collectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocPublisherShouldBeFound(String filter) throws Exception {
        restDocPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docPublisher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));

        // Check, that the count call also returns 1
        restDocPublisherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocPublisherShouldNotBeFound(String filter) throws Exception {
        restDocPublisherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocPublisherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocPublisher() throws Exception {
        // Get the docPublisher
        restDocPublisherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocPublisher() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();

        // Update the docPublisher
        DocPublisher updatedDocPublisher = docPublisherRepository.findById(docPublisher.getId()).get();
        // Disconnect from session so that the updates on updatedDocPublisher are not directly saved in db
        em.detach(updatedDocPublisher);
        updatedDocPublisher.name(UPDATED_NAME).notes(UPDATED_NOTES);

        restDocPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocPublisher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocPublisher))
            )
            .andExpect(status().isOk());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
        DocPublisher testDocPublisher = docPublisherList.get(docPublisherList.size() - 1);
        assertThat(testDocPublisher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocPublisher.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingDocPublisher() throws Exception {
        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();
        docPublisher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docPublisher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docPublisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocPublisher() throws Exception {
        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();
        docPublisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocPublisherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docPublisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocPublisher() throws Exception {
        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();
        docPublisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocPublisherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docPublisher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocPublisherWithPatch() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();

        // Update the docPublisher using partial update
        DocPublisher partialUpdatedDocPublisher = new DocPublisher();
        partialUpdatedDocPublisher.setId(docPublisher.getId());

        restDocPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocPublisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocPublisher))
            )
            .andExpect(status().isOk());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
        DocPublisher testDocPublisher = docPublisherList.get(docPublisherList.size() - 1);
        assertThat(testDocPublisher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocPublisher.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateDocPublisherWithPatch() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();

        // Update the docPublisher using partial update
        DocPublisher partialUpdatedDocPublisher = new DocPublisher();
        partialUpdatedDocPublisher.setId(docPublisher.getId());

        partialUpdatedDocPublisher.name(UPDATED_NAME).notes(UPDATED_NOTES);

        restDocPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocPublisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocPublisher))
            )
            .andExpect(status().isOk());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
        DocPublisher testDocPublisher = docPublisherList.get(docPublisherList.size() - 1);
        assertThat(testDocPublisher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocPublisher.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingDocPublisher() throws Exception {
        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();
        docPublisher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docPublisher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docPublisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocPublisher() throws Exception {
        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();
        docPublisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docPublisher))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocPublisher() throws Exception {
        int databaseSizeBeforeUpdate = docPublisherRepository.findAll().size();
        docPublisher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocPublisherMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docPublisher))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocPublisher in the database
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocPublisher() throws Exception {
        // Initialize the database
        docPublisherRepository.saveAndFlush(docPublisher);

        int databaseSizeBeforeDelete = docPublisherRepository.findAll().size();

        // Delete the docPublisher
        restDocPublisherMockMvc
            .perform(delete(ENTITY_API_URL_ID, docPublisher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocPublisher> docPublisherList = docPublisherRepository.findAll();
        assertThat(docPublisherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
