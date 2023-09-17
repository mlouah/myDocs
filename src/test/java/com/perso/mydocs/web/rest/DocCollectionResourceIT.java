package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.domain.DocCollection;
import com.perso.mydocs.domain.DocPublisher;
import com.perso.mydocs.repository.DocCollectionRepository;
import com.perso.mydocs.service.DocCollectionService;
import com.perso.mydocs.service.criteria.DocCollectionCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DocCollectionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocCollectionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-collections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocCollectionRepository docCollectionRepository;

    @Mock
    private DocCollectionRepository docCollectionRepositoryMock;

    @Mock
    private DocCollectionService docCollectionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocCollectionMockMvc;

    private DocCollection docCollection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocCollection createEntity(EntityManager em) {
        DocCollection docCollection = new DocCollection().name(DEFAULT_NAME).notes(DEFAULT_NOTES);
        return docCollection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocCollection createUpdatedEntity(EntityManager em) {
        DocCollection docCollection = new DocCollection().name(UPDATED_NAME).notes(UPDATED_NOTES);
        return docCollection;
    }

    @BeforeEach
    public void initTest() {
        docCollection = createEntity(em);
    }

    @Test
    @Transactional
    void createDocCollection() throws Exception {
        int databaseSizeBeforeCreate = docCollectionRepository.findAll().size();
        // Create the DocCollection
        restDocCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docCollection)))
            .andExpect(status().isCreated());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeCreate + 1);
        DocCollection testDocCollection = docCollectionList.get(docCollectionList.size() - 1);
        assertThat(testDocCollection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocCollection.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createDocCollectionWithExistingId() throws Exception {
        // Create the DocCollection with an existing ID
        docCollection.setId(1L);

        int databaseSizeBeforeCreate = docCollectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docCollection)))
            .andExpect(status().isBadRequest());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocCollections() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList
        restDocCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docCollection.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocCollectionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(docCollectionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocCollectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(docCollectionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocCollectionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(docCollectionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocCollectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(docCollectionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocCollection() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get the docCollection
        restDocCollectionMockMvc
            .perform(get(ENTITY_API_URL_ID, docCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docCollection.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getDocCollectionsByIdFiltering() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        Long id = docCollection.getId();

        defaultDocCollectionShouldBeFound("id.equals=" + id);
        defaultDocCollectionShouldNotBeFound("id.notEquals=" + id);

        defaultDocCollectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocCollectionShouldNotBeFound("id.greaterThan=" + id);

        defaultDocCollectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocCollectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where name equals to DEFAULT_NAME
        defaultDocCollectionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the docCollectionList where name equals to UPDATED_NAME
        defaultDocCollectionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocCollectionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the docCollectionList where name equals to UPDATED_NAME
        defaultDocCollectionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where name is not null
        defaultDocCollectionShouldBeFound("name.specified=true");

        // Get all the docCollectionList where name is null
        defaultDocCollectionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNameContainsSomething() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where name contains DEFAULT_NAME
        defaultDocCollectionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the docCollectionList where name contains UPDATED_NAME
        defaultDocCollectionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where name does not contain DEFAULT_NAME
        defaultDocCollectionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the docCollectionList where name does not contain UPDATED_NAME
        defaultDocCollectionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where notes equals to DEFAULT_NOTES
        defaultDocCollectionShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the docCollectionList where notes equals to UPDATED_NOTES
        defaultDocCollectionShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultDocCollectionShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the docCollectionList where notes equals to UPDATED_NOTES
        defaultDocCollectionShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where notes is not null
        defaultDocCollectionShouldBeFound("notes.specified=true");

        // Get all the docCollectionList where notes is null
        defaultDocCollectionShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNotesContainsSomething() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where notes contains DEFAULT_NOTES
        defaultDocCollectionShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the docCollectionList where notes contains UPDATED_NOTES
        defaultDocCollectionShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        // Get all the docCollectionList where notes does not contain DEFAULT_NOTES
        defaultDocCollectionShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the docCollectionList where notes does not contain UPDATED_NOTES
        defaultDocCollectionShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocCollectionsByDocIsEqualToSomething() throws Exception {
        Doc doc;
        if (TestUtil.findAll(em, Doc.class).isEmpty()) {
            docCollectionRepository.saveAndFlush(docCollection);
            doc = DocResourceIT.createEntity(em);
        } else {
            doc = TestUtil.findAll(em, Doc.class).get(0);
        }
        em.persist(doc);
        em.flush();
        docCollection.setDoc(doc);
        doc.setCollection(docCollection);
        docCollectionRepository.saveAndFlush(docCollection);
        Long docId = doc.getId();

        // Get all the docCollectionList where doc equals to docId
        defaultDocCollectionShouldBeFound("docId.equals=" + docId);

        // Get all the docCollectionList where doc equals to (docId + 1)
        defaultDocCollectionShouldNotBeFound("docId.equals=" + (docId + 1));
    }

    @Test
    @Transactional
    void getAllDocCollectionsByDocPublisherIsEqualToSomething() throws Exception {
        DocPublisher docPublisher;
        if (TestUtil.findAll(em, DocPublisher.class).isEmpty()) {
            docCollectionRepository.saveAndFlush(docCollection);
            docPublisher = DocPublisherResourceIT.createEntity(em);
        } else {
            docPublisher = TestUtil.findAll(em, DocPublisher.class).get(0);
        }
        em.persist(docPublisher);
        em.flush();
        docCollection.setDocPublisher(docPublisher);
        docCollectionRepository.saveAndFlush(docCollection);
        Long docPublisherId = docPublisher.getId();

        // Get all the docCollectionList where docPublisher equals to docPublisherId
        defaultDocCollectionShouldBeFound("docPublisherId.equals=" + docPublisherId);

        // Get all the docCollectionList where docPublisher equals to (docPublisherId + 1)
        defaultDocCollectionShouldNotBeFound("docPublisherId.equals=" + (docPublisherId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocCollectionShouldBeFound(String filter) throws Exception {
        restDocCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docCollection.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restDocCollectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocCollectionShouldNotBeFound(String filter) throws Exception {
        restDocCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocCollectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocCollection() throws Exception {
        // Get the docCollection
        restDocCollectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocCollection() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();

        // Update the docCollection
        DocCollection updatedDocCollection = docCollectionRepository.findById(docCollection.getId()).get();
        // Disconnect from session so that the updates on updatedDocCollection are not directly saved in db
        em.detach(updatedDocCollection);
        updatedDocCollection.name(UPDATED_NAME).notes(UPDATED_NOTES);

        restDocCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocCollection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocCollection))
            )
            .andExpect(status().isOk());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
        DocCollection testDocCollection = docCollectionList.get(docCollectionList.size() - 1);
        assertThat(testDocCollection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocCollection.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingDocCollection() throws Exception {
        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();
        docCollection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docCollection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocCollection() throws Exception {
        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();
        docCollection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocCollection() throws Exception {
        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();
        docCollection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCollectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docCollection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocCollectionWithPatch() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();

        // Update the docCollection using partial update
        DocCollection partialUpdatedDocCollection = new DocCollection();
        partialUpdatedDocCollection.setId(docCollection.getId());

        partialUpdatedDocCollection.notes(UPDATED_NOTES);

        restDocCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocCollection))
            )
            .andExpect(status().isOk());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
        DocCollection testDocCollection = docCollectionList.get(docCollectionList.size() - 1);
        assertThat(testDocCollection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocCollection.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateDocCollectionWithPatch() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();

        // Update the docCollection using partial update
        DocCollection partialUpdatedDocCollection = new DocCollection();
        partialUpdatedDocCollection.setId(docCollection.getId());

        partialUpdatedDocCollection.name(UPDATED_NAME).notes(UPDATED_NOTES);

        restDocCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocCollection))
            )
            .andExpect(status().isOk());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
        DocCollection testDocCollection = docCollectionList.get(docCollectionList.size() - 1);
        assertThat(testDocCollection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocCollection.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingDocCollection() throws Exception {
        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();
        docCollection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocCollection() throws Exception {
        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();
        docCollection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocCollection() throws Exception {
        int databaseSizeBeforeUpdate = docCollectionRepository.findAll().size();
        docCollection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docCollection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocCollection in the database
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocCollection() throws Exception {
        // Initialize the database
        docCollectionRepository.saveAndFlush(docCollection);

        int databaseSizeBeforeDelete = docCollectionRepository.findAll().size();

        // Delete the docCollection
        restDocCollectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, docCollection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocCollection> docCollectionList = docCollectionRepository.findAll();
        assertThat(docCollectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
