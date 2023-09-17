package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.domain.DocFormat;
import com.perso.mydocs.repository.DocFormatRepository;
import com.perso.mydocs.service.criteria.DocFormatCriteria;
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

/**
 * Integration tests for the {@link DocFormatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocFormatResourceIT {

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-formats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocFormatRepository docFormatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocFormatMockMvc;

    private DocFormat docFormat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocFormat createEntity(EntityManager em) {
        DocFormat docFormat = new DocFormat().format(DEFAULT_FORMAT).code(DEFAULT_CODE).notes(DEFAULT_NOTES);
        return docFormat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocFormat createUpdatedEntity(EntityManager em) {
        DocFormat docFormat = new DocFormat().format(UPDATED_FORMAT).code(UPDATED_CODE).notes(UPDATED_NOTES);
        return docFormat;
    }

    @BeforeEach
    public void initTest() {
        docFormat = createEntity(em);
    }

    @Test
    @Transactional
    void createDocFormat() throws Exception {
        int databaseSizeBeforeCreate = docFormatRepository.findAll().size();
        // Create the DocFormat
        restDocFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docFormat)))
            .andExpect(status().isCreated());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeCreate + 1);
        DocFormat testDocFormat = docFormatList.get(docFormatList.size() - 1);
        assertThat(testDocFormat.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testDocFormat.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDocFormat.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createDocFormatWithExistingId() throws Exception {
        // Create the DocFormat with an existing ID
        docFormat.setId(1L);

        int databaseSizeBeforeCreate = docFormatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocFormatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docFormat)))
            .andExpect(status().isBadRequest());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocFormats() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList
        restDocFormatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docFormat.getId().intValue())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getDocFormat() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get the docFormat
        restDocFormatMockMvc
            .perform(get(ENTITY_API_URL_ID, docFormat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docFormat.getId().intValue()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getDocFormatsByIdFiltering() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        Long id = docFormat.getId();

        defaultDocFormatShouldBeFound("id.equals=" + id);
        defaultDocFormatShouldNotBeFound("id.notEquals=" + id);

        defaultDocFormatShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocFormatShouldNotBeFound("id.greaterThan=" + id);

        defaultDocFormatShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocFormatShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocFormatsByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where format equals to DEFAULT_FORMAT
        defaultDocFormatShouldBeFound("format.equals=" + DEFAULT_FORMAT);

        // Get all the docFormatList where format equals to UPDATED_FORMAT
        defaultDocFormatShouldNotBeFound("format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllDocFormatsByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where format in DEFAULT_FORMAT or UPDATED_FORMAT
        defaultDocFormatShouldBeFound("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT);

        // Get all the docFormatList where format equals to UPDATED_FORMAT
        defaultDocFormatShouldNotBeFound("format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllDocFormatsByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where format is not null
        defaultDocFormatShouldBeFound("format.specified=true");

        // Get all the docFormatList where format is null
        defaultDocFormatShouldNotBeFound("format.specified=false");
    }

    @Test
    @Transactional
    void getAllDocFormatsByFormatContainsSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where format contains DEFAULT_FORMAT
        defaultDocFormatShouldBeFound("format.contains=" + DEFAULT_FORMAT);

        // Get all the docFormatList where format contains UPDATED_FORMAT
        defaultDocFormatShouldNotBeFound("format.contains=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllDocFormatsByFormatNotContainsSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where format does not contain DEFAULT_FORMAT
        defaultDocFormatShouldNotBeFound("format.doesNotContain=" + DEFAULT_FORMAT);

        // Get all the docFormatList where format does not contain UPDATED_FORMAT
        defaultDocFormatShouldBeFound("format.doesNotContain=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllDocFormatsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where code equals to DEFAULT_CODE
        defaultDocFormatShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the docFormatList where code equals to UPDATED_CODE
        defaultDocFormatShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocFormatsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDocFormatShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the docFormatList where code equals to UPDATED_CODE
        defaultDocFormatShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocFormatsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where code is not null
        defaultDocFormatShouldBeFound("code.specified=true");

        // Get all the docFormatList where code is null
        defaultDocFormatShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDocFormatsByCodeContainsSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where code contains DEFAULT_CODE
        defaultDocFormatShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the docFormatList where code contains UPDATED_CODE
        defaultDocFormatShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocFormatsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where code does not contain DEFAULT_CODE
        defaultDocFormatShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the docFormatList where code does not contain UPDATED_CODE
        defaultDocFormatShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocFormatsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where notes equals to DEFAULT_NOTES
        defaultDocFormatShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the docFormatList where notes equals to UPDATED_NOTES
        defaultDocFormatShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocFormatsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultDocFormatShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the docFormatList where notes equals to UPDATED_NOTES
        defaultDocFormatShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocFormatsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where notes is not null
        defaultDocFormatShouldBeFound("notes.specified=true");

        // Get all the docFormatList where notes is null
        defaultDocFormatShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDocFormatsByNotesContainsSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where notes contains DEFAULT_NOTES
        defaultDocFormatShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the docFormatList where notes contains UPDATED_NOTES
        defaultDocFormatShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocFormatsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        // Get all the docFormatList where notes does not contain DEFAULT_NOTES
        defaultDocFormatShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the docFormatList where notes does not contain UPDATED_NOTES
        defaultDocFormatShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocFormatsByDocIsEqualToSomething() throws Exception {
        Doc doc;
        if (TestUtil.findAll(em, Doc.class).isEmpty()) {
            docFormatRepository.saveAndFlush(docFormat);
            doc = DocResourceIT.createEntity(em);
        } else {
            doc = TestUtil.findAll(em, Doc.class).get(0);
        }
        em.persist(doc);
        em.flush();
        docFormat.addDoc(doc);
        docFormatRepository.saveAndFlush(docFormat);
        Long docId = doc.getId();

        // Get all the docFormatList where doc equals to docId
        defaultDocFormatShouldBeFound("docId.equals=" + docId);

        // Get all the docFormatList where doc equals to (docId + 1)
        defaultDocFormatShouldNotBeFound("docId.equals=" + (docId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocFormatShouldBeFound(String filter) throws Exception {
        restDocFormatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docFormat.getId().intValue())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restDocFormatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocFormatShouldNotBeFound(String filter) throws Exception {
        restDocFormatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocFormatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocFormat() throws Exception {
        // Get the docFormat
        restDocFormatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocFormat() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();

        // Update the docFormat
        DocFormat updatedDocFormat = docFormatRepository.findById(docFormat.getId()).get();
        // Disconnect from session so that the updates on updatedDocFormat are not directly saved in db
        em.detach(updatedDocFormat);
        updatedDocFormat.format(UPDATED_FORMAT).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restDocFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocFormat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocFormat))
            )
            .andExpect(status().isOk());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
        DocFormat testDocFormat = docFormatList.get(docFormatList.size() - 1);
        assertThat(testDocFormat.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDocFormat.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocFormat.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingDocFormat() throws Exception {
        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();
        docFormat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docFormat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocFormat() throws Exception {
        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();
        docFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocFormatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocFormat() throws Exception {
        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();
        docFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocFormatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docFormat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocFormatWithPatch() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();

        // Update the docFormat using partial update
        DocFormat partialUpdatedDocFormat = new DocFormat();
        partialUpdatedDocFormat.setId(docFormat.getId());

        partialUpdatedDocFormat.code(UPDATED_CODE);

        restDocFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocFormat))
            )
            .andExpect(status().isOk());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
        DocFormat testDocFormat = docFormatList.get(docFormatList.size() - 1);
        assertThat(testDocFormat.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testDocFormat.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocFormat.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateDocFormatWithPatch() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();

        // Update the docFormat using partial update
        DocFormat partialUpdatedDocFormat = new DocFormat();
        partialUpdatedDocFormat.setId(docFormat.getId());

        partialUpdatedDocFormat.format(UPDATED_FORMAT).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restDocFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocFormat))
            )
            .andExpect(status().isOk());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
        DocFormat testDocFormat = docFormatList.get(docFormatList.size() - 1);
        assertThat(testDocFormat.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testDocFormat.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocFormat.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingDocFormat() throws Exception {
        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();
        docFormat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docFormat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocFormat() throws Exception {
        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();
        docFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocFormatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docFormat))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocFormat() throws Exception {
        int databaseSizeBeforeUpdate = docFormatRepository.findAll().size();
        docFormat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocFormatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docFormat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocFormat in the database
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocFormat() throws Exception {
        // Initialize the database
        docFormatRepository.saveAndFlush(docFormat);

        int databaseSizeBeforeDelete = docFormatRepository.findAll().size();

        // Delete the docFormat
        restDocFormatMockMvc
            .perform(delete(ENTITY_API_URL_ID, docFormat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocFormat> docFormatList = docFormatRepository.findAll();
        assertThat(docFormatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
