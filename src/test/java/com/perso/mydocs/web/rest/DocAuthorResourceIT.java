package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.domain.DocAuthor;
import com.perso.mydocs.repository.DocAuthorRepository;
import com.perso.mydocs.service.criteria.DocAuthorCriteria;
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
 * Integration tests for the {@link DocAuthorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocAuthorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-authors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocAuthorRepository docAuthorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocAuthorMockMvc;

    private DocAuthor docAuthor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocAuthor createEntity(EntityManager em) {
        DocAuthor docAuthor = new DocAuthor().name(DEFAULT_NAME).imgUrl(DEFAULT_IMG_URL).notes(DEFAULT_NOTES).url(DEFAULT_URL);
        return docAuthor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocAuthor createUpdatedEntity(EntityManager em) {
        DocAuthor docAuthor = new DocAuthor().name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL).notes(UPDATED_NOTES).url(UPDATED_URL);
        return docAuthor;
    }

    @BeforeEach
    public void initTest() {
        docAuthor = createEntity(em);
    }

    @Test
    @Transactional
    void createDocAuthor() throws Exception {
        int databaseSizeBeforeCreate = docAuthorRepository.findAll().size();
        // Create the DocAuthor
        restDocAuthorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docAuthor)))
            .andExpect(status().isCreated());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeCreate + 1);
        DocAuthor testDocAuthor = docAuthorList.get(docAuthorList.size() - 1);
        assertThat(testDocAuthor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocAuthor.getImgUrl()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testDocAuthor.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDocAuthor.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createDocAuthorWithExistingId() throws Exception {
        // Create the DocAuthor with an existing ID
        docAuthor.setId(1L);

        int databaseSizeBeforeCreate = docAuthorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocAuthorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docAuthor)))
            .andExpect(status().isBadRequest());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocAuthors() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList
        restDocAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docAuthor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getDocAuthor() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get the docAuthor
        restDocAuthorMockMvc
            .perform(get(ENTITY_API_URL_ID, docAuthor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docAuthor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imgUrl").value(DEFAULT_IMG_URL))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getDocAuthorsByIdFiltering() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        Long id = docAuthor.getId();

        defaultDocAuthorShouldBeFound("id.equals=" + id);
        defaultDocAuthorShouldNotBeFound("id.notEquals=" + id);

        defaultDocAuthorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocAuthorShouldNotBeFound("id.greaterThan=" + id);

        defaultDocAuthorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocAuthorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where name equals to DEFAULT_NAME
        defaultDocAuthorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the docAuthorList where name equals to UPDATED_NAME
        defaultDocAuthorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocAuthorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the docAuthorList where name equals to UPDATED_NAME
        defaultDocAuthorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where name is not null
        defaultDocAuthorShouldBeFound("name.specified=true");

        // Get all the docAuthorList where name is null
        defaultDocAuthorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocAuthorsByNameContainsSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where name contains DEFAULT_NAME
        defaultDocAuthorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the docAuthorList where name contains UPDATED_NAME
        defaultDocAuthorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where name does not contain DEFAULT_NAME
        defaultDocAuthorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the docAuthorList where name does not contain UPDATED_NAME
        defaultDocAuthorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where imgUrl equals to DEFAULT_IMG_URL
        defaultDocAuthorShouldBeFound("imgUrl.equals=" + DEFAULT_IMG_URL);

        // Get all the docAuthorList where imgUrl equals to UPDATED_IMG_URL
        defaultDocAuthorShouldNotBeFound("imgUrl.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where imgUrl in DEFAULT_IMG_URL or UPDATED_IMG_URL
        defaultDocAuthorShouldBeFound("imgUrl.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL);

        // Get all the docAuthorList where imgUrl equals to UPDATED_IMG_URL
        defaultDocAuthorShouldNotBeFound("imgUrl.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where imgUrl is not null
        defaultDocAuthorShouldBeFound("imgUrl.specified=true");

        // Get all the docAuthorList where imgUrl is null
        defaultDocAuthorShouldNotBeFound("imgUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllDocAuthorsByImgUrlContainsSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where imgUrl contains DEFAULT_IMG_URL
        defaultDocAuthorShouldBeFound("imgUrl.contains=" + DEFAULT_IMG_URL);

        // Get all the docAuthorList where imgUrl contains UPDATED_IMG_URL
        defaultDocAuthorShouldNotBeFound("imgUrl.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where imgUrl does not contain DEFAULT_IMG_URL
        defaultDocAuthorShouldNotBeFound("imgUrl.doesNotContain=" + DEFAULT_IMG_URL);

        // Get all the docAuthorList where imgUrl does not contain UPDATED_IMG_URL
        defaultDocAuthorShouldBeFound("imgUrl.doesNotContain=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where url equals to DEFAULT_URL
        defaultDocAuthorShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the docAuthorList where url equals to UPDATED_URL
        defaultDocAuthorShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where url in DEFAULT_URL or UPDATED_URL
        defaultDocAuthorShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the docAuthorList where url equals to UPDATED_URL
        defaultDocAuthorShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where url is not null
        defaultDocAuthorShouldBeFound("url.specified=true");

        // Get all the docAuthorList where url is null
        defaultDocAuthorShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllDocAuthorsByUrlContainsSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where url contains DEFAULT_URL
        defaultDocAuthorShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the docAuthorList where url contains UPDATED_URL
        defaultDocAuthorShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        // Get all the docAuthorList where url does not contain DEFAULT_URL
        defaultDocAuthorShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the docAuthorList where url does not contain UPDATED_URL
        defaultDocAuthorShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllDocAuthorsByDocIsEqualToSomething() throws Exception {
        Doc doc;
        if (TestUtil.findAll(em, Doc.class).isEmpty()) {
            docAuthorRepository.saveAndFlush(docAuthor);
            doc = DocResourceIT.createEntity(em);
        } else {
            doc = TestUtil.findAll(em, Doc.class).get(0);
        }
        em.persist(doc);
        em.flush();
        docAuthor.addDoc(doc);
        docAuthorRepository.saveAndFlush(docAuthor);
        Long docId = doc.getId();

        // Get all the docAuthorList where doc equals to docId
        defaultDocAuthorShouldBeFound("docId.equals=" + docId);

        // Get all the docAuthorList where doc equals to (docId + 1)
        defaultDocAuthorShouldNotBeFound("docId.equals=" + (docId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocAuthorShouldBeFound(String filter) throws Exception {
        restDocAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docAuthor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restDocAuthorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocAuthorShouldNotBeFound(String filter) throws Exception {
        restDocAuthorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocAuthorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocAuthor() throws Exception {
        // Get the docAuthor
        restDocAuthorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocAuthor() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();

        // Update the docAuthor
        DocAuthor updatedDocAuthor = docAuthorRepository.findById(docAuthor.getId()).get();
        // Disconnect from session so that the updates on updatedDocAuthor are not directly saved in db
        em.detach(updatedDocAuthor);
        updatedDocAuthor.name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL).notes(UPDATED_NOTES).url(UPDATED_URL);

        restDocAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocAuthor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocAuthor))
            )
            .andExpect(status().isOk());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
        DocAuthor testDocAuthor = docAuthorList.get(docAuthorList.size() - 1);
        assertThat(testDocAuthor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocAuthor.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testDocAuthor.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocAuthor.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingDocAuthor() throws Exception {
        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();
        docAuthor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docAuthor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocAuthor() throws Exception {
        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();
        docAuthor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocAuthorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocAuthor() throws Exception {
        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();
        docAuthor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocAuthorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docAuthor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocAuthorWithPatch() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();

        // Update the docAuthor using partial update
        DocAuthor partialUpdatedDocAuthor = new DocAuthor();
        partialUpdatedDocAuthor.setId(docAuthor.getId());

        partialUpdatedDocAuthor.name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL).notes(UPDATED_NOTES);

        restDocAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocAuthor))
            )
            .andExpect(status().isOk());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
        DocAuthor testDocAuthor = docAuthorList.get(docAuthorList.size() - 1);
        assertThat(testDocAuthor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocAuthor.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testDocAuthor.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocAuthor.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void fullUpdateDocAuthorWithPatch() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();

        // Update the docAuthor using partial update
        DocAuthor partialUpdatedDocAuthor = new DocAuthor();
        partialUpdatedDocAuthor.setId(docAuthor.getId());

        partialUpdatedDocAuthor.name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL).notes(UPDATED_NOTES).url(UPDATED_URL);

        restDocAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocAuthor))
            )
            .andExpect(status().isOk());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
        DocAuthor testDocAuthor = docAuthorList.get(docAuthorList.size() - 1);
        assertThat(testDocAuthor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocAuthor.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testDocAuthor.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDocAuthor.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingDocAuthor() throws Exception {
        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();
        docAuthor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docAuthor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocAuthor() throws Exception {
        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();
        docAuthor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docAuthor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocAuthor() throws Exception {
        int databaseSizeBeforeUpdate = docAuthorRepository.findAll().size();
        docAuthor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocAuthorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docAuthor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocAuthor in the database
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocAuthor() throws Exception {
        // Initialize the database
        docAuthorRepository.saveAndFlush(docAuthor);

        int databaseSizeBeforeDelete = docAuthorRepository.findAll().size();

        // Delete the docAuthor
        restDocAuthorMockMvc
            .perform(delete(ENTITY_API_URL_ID, docAuthor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocAuthor> docAuthorList = docAuthorRepository.findAll();
        assertThat(docAuthorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
