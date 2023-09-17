package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.domain.DocCategory;
import com.perso.mydocs.repository.DocCategoryRepository;
import com.perso.mydocs.service.criteria.DocCategoryCriteria;
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
 * Integration tests for the {@link DocCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocCategoryRepository docCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocCategoryMockMvc;

    private DocCategory docCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocCategory createEntity(EntityManager em) {
        DocCategory docCategory = new DocCategory().name(DEFAULT_NAME).code(DEFAULT_CODE).notes(DEFAULT_NOTES);
        return docCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocCategory createUpdatedEntity(EntityManager em) {
        DocCategory docCategory = new DocCategory().name(UPDATED_NAME).code(UPDATED_CODE).notes(UPDATED_NOTES);
        return docCategory;
    }

    @BeforeEach
    public void initTest() {
        docCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createDocCategory() throws Exception {
        int databaseSizeBeforeCreate = docCategoryRepository.findAll().size();
        // Create the DocCategory
        restDocCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docCategory)))
            .andExpect(status().isCreated());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        DocCategory testDocCategory = docCategoryList.get(docCategoryList.size() - 1);
        assertThat(testDocCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDocCategory.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createDocCategoryWithExistingId() throws Exception {
        // Create the DocCategory with an existing ID
        docCategory.setId(1L);

        int databaseSizeBeforeCreate = docCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docCategory)))
            .andExpect(status().isBadRequest());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocCategories() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList
        restDocCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    void getDocCategory() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get the docCategory
        restDocCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, docCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    void getDocCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        Long id = docCategory.getId();

        defaultDocCategoryShouldBeFound("id.equals=" + id);
        defaultDocCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultDocCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultDocCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where name equals to DEFAULT_NAME
        defaultDocCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the docCategoryList where name equals to UPDATED_NAME
        defaultDocCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the docCategoryList where name equals to UPDATED_NAME
        defaultDocCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where name is not null
        defaultDocCategoryShouldBeFound("name.specified=true");

        // Get all the docCategoryList where name is null
        defaultDocCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where name contains DEFAULT_NAME
        defaultDocCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the docCategoryList where name contains UPDATED_NAME
        defaultDocCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where name does not contain DEFAULT_NAME
        defaultDocCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the docCategoryList where name does not contain UPDATED_NAME
        defaultDocCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where code equals to DEFAULT_CODE
        defaultDocCategoryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the docCategoryList where code equals to UPDATED_CODE
        defaultDocCategoryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDocCategoryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the docCategoryList where code equals to UPDATED_CODE
        defaultDocCategoryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where code is not null
        defaultDocCategoryShouldBeFound("code.specified=true");

        // Get all the docCategoryList where code is null
        defaultDocCategoryShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDocCategoriesByCodeContainsSomething() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where code contains DEFAULT_CODE
        defaultDocCategoryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the docCategoryList where code contains UPDATED_CODE
        defaultDocCategoryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        // Get all the docCategoryList where code does not contain DEFAULT_CODE
        defaultDocCategoryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the docCategoryList where code does not contain UPDATED_CODE
        defaultDocCategoryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocCategoriesByDocIsEqualToSomething() throws Exception {
        Doc doc;
        if (TestUtil.findAll(em, Doc.class).isEmpty()) {
            docCategoryRepository.saveAndFlush(docCategory);
            doc = DocResourceIT.createEntity(em);
        } else {
            doc = TestUtil.findAll(em, Doc.class).get(0);
        }
        em.persist(doc);
        em.flush();
        docCategory.addDoc(doc);
        docCategoryRepository.saveAndFlush(docCategory);
        Long docId = doc.getId();

        // Get all the docCategoryList where doc equals to docId
        defaultDocCategoryShouldBeFound("docId.equals=" + docId);

        // Get all the docCategoryList where doc equals to (docId + 1)
        defaultDocCategoryShouldNotBeFound("docId.equals=" + (docId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocCategoryShouldBeFound(String filter) throws Exception {
        restDocCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));

        // Check, that the count call also returns 1
        restDocCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocCategoryShouldNotBeFound(String filter) throws Exception {
        restDocCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocCategory() throws Exception {
        // Get the docCategory
        restDocCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocCategory() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();

        // Update the docCategory
        DocCategory updatedDocCategory = docCategoryRepository.findById(docCategory.getId()).get();
        // Disconnect from session so that the updates on updatedDocCategory are not directly saved in db
        em.detach(updatedDocCategory);
        updatedDocCategory.name(UPDATED_NAME).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restDocCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocCategory))
            )
            .andExpect(status().isOk());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
        DocCategory testDocCategory = docCategoryList.get(docCategoryList.size() - 1);
        assertThat(testDocCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocCategory.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingDocCategory() throws Exception {
        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();
        docCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocCategory() throws Exception {
        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();
        docCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocCategory() throws Exception {
        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();
        docCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocCategoryWithPatch() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();

        // Update the docCategory using partial update
        DocCategory partialUpdatedDocCategory = new DocCategory();
        partialUpdatedDocCategory.setId(docCategory.getId());

        partialUpdatedDocCategory.name(UPDATED_NAME).notes(UPDATED_NOTES);

        restDocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocCategory))
            )
            .andExpect(status().isOk());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
        DocCategory testDocCategory = docCategoryList.get(docCategoryList.size() - 1);
        assertThat(testDocCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDocCategory.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateDocCategoryWithPatch() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();

        // Update the docCategory using partial update
        DocCategory partialUpdatedDocCategory = new DocCategory();
        partialUpdatedDocCategory.setId(docCategory.getId());

        partialUpdatedDocCategory.name(UPDATED_NAME).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restDocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocCategory))
            )
            .andExpect(status().isOk());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
        DocCategory testDocCategory = docCategoryList.get(docCategoryList.size() - 1);
        assertThat(testDocCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocCategory.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingDocCategory() throws Exception {
        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();
        docCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocCategory() throws Exception {
        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();
        docCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocCategory() throws Exception {
        int databaseSizeBeforeUpdate = docCategoryRepository.findAll().size();
        docCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocCategory in the database
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocCategory() throws Exception {
        // Initialize the database
        docCategoryRepository.saveAndFlush(docCategory);

        int databaseSizeBeforeDelete = docCategoryRepository.findAll().size();

        // Delete the docCategory
        restDocCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, docCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocCategory> docCategoryList = docCategoryRepository.findAll();
        assertThat(docCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
