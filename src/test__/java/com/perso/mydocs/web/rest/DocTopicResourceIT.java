package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.domain.DocTopic;
import com.perso.mydocs.domain.Domaine;
import com.perso.mydocs.repository.DocTopicRepository;
import com.perso.mydocs.service.DocTopicService;
import com.perso.mydocs.service.criteria.DocTopicCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocTopicResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocTopicResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-topics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocTopicRepository docTopicRepository;

    @Mock
    private DocTopicRepository docTopicRepositoryMock;

    @Mock
    private DocTopicService docTopicServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocTopicMockMvc;

    private DocTopic docTopic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocTopic createEntity(EntityManager em) {
        DocTopic docTopic = new DocTopic().name(DEFAULT_NAME).code(DEFAULT_CODE).imgUrl(DEFAULT_IMG_URL).notes(DEFAULT_NOTES);
        return docTopic;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocTopic createUpdatedEntity(EntityManager em) {
        DocTopic docTopic = new DocTopic().name(UPDATED_NAME).code(UPDATED_CODE).imgUrl(UPDATED_IMG_URL).notes(UPDATED_NOTES);
        return docTopic;
    }

    @BeforeEach
    public void initTest() {
        docTopic = createEntity(em);
    }

    @Test
    @Transactional
    void createDocTopic() throws Exception {
        int databaseSizeBeforeCreate = docTopicRepository.findAll().size();
        // Create the DocTopic
        restDocTopicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docTopic)))
            .andExpect(status().isCreated());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeCreate + 1);
        DocTopic testDocTopic = docTopicList.get(docTopicList.size() - 1);
        assertThat(testDocTopic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocTopic.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDocTopic.getImgUrl()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testDocTopic.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createDocTopicWithExistingId() throws Exception {
        // Create the DocTopic with an existing ID
        docTopic.setId(1L);

        int databaseSizeBeforeCreate = docTopicRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocTopicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docTopic)))
            .andExpect(status().isBadRequest());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocTopics() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList
        restDocTopicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docTopic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocTopicsWithEagerRelationshipsIsEnabled() throws Exception {
        when(docTopicServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocTopicMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(docTopicServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocTopicsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(docTopicServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocTopicMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(docTopicRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocTopic() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get the docTopic
        restDocTopicMockMvc
            .perform(get(ENTITY_API_URL_ID, docTopic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docTopic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.imgUrl").value(DEFAULT_IMG_URL))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    void getDocTopicsByIdFiltering() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        Long id = docTopic.getId();

        defaultDocTopicShouldBeFound("id.equals=" + id);
        defaultDocTopicShouldNotBeFound("id.notEquals=" + id);

        defaultDocTopicShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocTopicShouldNotBeFound("id.greaterThan=" + id);

        defaultDocTopicShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocTopicShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocTopicsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where name equals to DEFAULT_NAME
        defaultDocTopicShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the docTopicList where name equals to UPDATED_NAME
        defaultDocTopicShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocTopicsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocTopicShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the docTopicList where name equals to UPDATED_NAME
        defaultDocTopicShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocTopicsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where name is not null
        defaultDocTopicShouldBeFound("name.specified=true");

        // Get all the docTopicList where name is null
        defaultDocTopicShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocTopicsByNameContainsSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where name contains DEFAULT_NAME
        defaultDocTopicShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the docTopicList where name contains UPDATED_NAME
        defaultDocTopicShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocTopicsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where name does not contain DEFAULT_NAME
        defaultDocTopicShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the docTopicList where name does not contain UPDATED_NAME
        defaultDocTopicShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocTopicsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where code equals to DEFAULT_CODE
        defaultDocTopicShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the docTopicList where code equals to UPDATED_CODE
        defaultDocTopicShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocTopicsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDocTopicShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the docTopicList where code equals to UPDATED_CODE
        defaultDocTopicShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocTopicsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where code is not null
        defaultDocTopicShouldBeFound("code.specified=true");

        // Get all the docTopicList where code is null
        defaultDocTopicShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDocTopicsByCodeContainsSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where code contains DEFAULT_CODE
        defaultDocTopicShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the docTopicList where code contains UPDATED_CODE
        defaultDocTopicShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocTopicsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where code does not contain DEFAULT_CODE
        defaultDocTopicShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the docTopicList where code does not contain UPDATED_CODE
        defaultDocTopicShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocTopicsByImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where imgUrl equals to DEFAULT_IMG_URL
        defaultDocTopicShouldBeFound("imgUrl.equals=" + DEFAULT_IMG_URL);

        // Get all the docTopicList where imgUrl equals to UPDATED_IMG_URL
        defaultDocTopicShouldNotBeFound("imgUrl.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocTopicsByImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where imgUrl in DEFAULT_IMG_URL or UPDATED_IMG_URL
        defaultDocTopicShouldBeFound("imgUrl.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL);

        // Get all the docTopicList where imgUrl equals to UPDATED_IMG_URL
        defaultDocTopicShouldNotBeFound("imgUrl.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocTopicsByImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where imgUrl is not null
        defaultDocTopicShouldBeFound("imgUrl.specified=true");

        // Get all the docTopicList where imgUrl is null
        defaultDocTopicShouldNotBeFound("imgUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllDocTopicsByImgUrlContainsSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where imgUrl contains DEFAULT_IMG_URL
        defaultDocTopicShouldBeFound("imgUrl.contains=" + DEFAULT_IMG_URL);

        // Get all the docTopicList where imgUrl contains UPDATED_IMG_URL
        defaultDocTopicShouldNotBeFound("imgUrl.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocTopicsByImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        // Get all the docTopicList where imgUrl does not contain DEFAULT_IMG_URL
        defaultDocTopicShouldNotBeFound("imgUrl.doesNotContain=" + DEFAULT_IMG_URL);

        // Get all the docTopicList where imgUrl does not contain UPDATED_IMG_URL
        defaultDocTopicShouldBeFound("imgUrl.doesNotContain=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllDocTopicsByDocIsEqualToSomething() throws Exception {
        Doc doc;
        if (TestUtil.findAll(em, Doc.class).isEmpty()) {
            docTopicRepository.saveAndFlush(docTopic);
            doc = DocResourceIT.createEntity(em);
        } else {
            doc = TestUtil.findAll(em, Doc.class).get(0);
        }
        em.persist(doc);
        em.flush();
        docTopic.addDoc(doc);
        docTopicRepository.saveAndFlush(docTopic);
        Long docId = doc.getId();

        // Get all the docTopicList where doc equals to docId
        defaultDocTopicShouldBeFound("docId.equals=" + docId);

        // Get all the docTopicList where doc equals to (docId + 1)
        defaultDocTopicShouldNotBeFound("docId.equals=" + (docId + 1));
    }

    @Test
    @Transactional
    void getAllDocTopicsByDomaineIsEqualToSomething() throws Exception {
        Domaine domaine;
        if (TestUtil.findAll(em, Domaine.class).isEmpty()) {
            docTopicRepository.saveAndFlush(docTopic);
            domaine = DomaineResourceIT.createEntity(em);
        } else {
            domaine = TestUtil.findAll(em, Domaine.class).get(0);
        }
        em.persist(domaine);
        em.flush();
        docTopic.setDomaine(domaine);
        docTopicRepository.saveAndFlush(docTopic);
        Long domaineId = domaine.getId();

        // Get all the docTopicList where domaine equals to domaineId
        defaultDocTopicShouldBeFound("domaineId.equals=" + domaineId);

        // Get all the docTopicList where domaine equals to (domaineId + 1)
        defaultDocTopicShouldNotBeFound("domaineId.equals=" + (domaineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocTopicShouldBeFound(String filter) throws Exception {
        restDocTopicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docTopic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));

        // Check, that the count call also returns 1
        restDocTopicMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocTopicShouldNotBeFound(String filter) throws Exception {
        restDocTopicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocTopicMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocTopic() throws Exception {
        // Get the docTopic
        restDocTopicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocTopic() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();

        // Update the docTopic
        DocTopic updatedDocTopic = docTopicRepository.findById(docTopic.getId()).get();
        // Disconnect from session so that the updates on updatedDocTopic are not directly saved in db
        em.detach(updatedDocTopic);
        updatedDocTopic.name(UPDATED_NAME).code(UPDATED_CODE).imgUrl(UPDATED_IMG_URL).notes(UPDATED_NOTES);

        restDocTopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocTopic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocTopic))
            )
            .andExpect(status().isOk());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
        DocTopic testDocTopic = docTopicList.get(docTopicList.size() - 1);
        assertThat(testDocTopic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocTopic.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocTopic.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testDocTopic.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingDocTopic() throws Exception {
        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();
        docTopic.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocTopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docTopic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docTopic))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocTopic() throws Exception {
        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();
        docTopic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTopicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docTopic))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocTopic() throws Exception {
        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();
        docTopic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTopicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docTopic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocTopicWithPatch() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();

        // Update the docTopic using partial update
        DocTopic partialUpdatedDocTopic = new DocTopic();
        partialUpdatedDocTopic.setId(docTopic.getId());

        partialUpdatedDocTopic.name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL);

        restDocTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocTopic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocTopic))
            )
            .andExpect(status().isOk());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
        DocTopic testDocTopic = docTopicList.get(docTopicList.size() - 1);
        assertThat(testDocTopic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocTopic.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDocTopic.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testDocTopic.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateDocTopicWithPatch() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();

        // Update the docTopic using partial update
        DocTopic partialUpdatedDocTopic = new DocTopic();
        partialUpdatedDocTopic.setId(docTopic.getId());

        partialUpdatedDocTopic.name(UPDATED_NAME).code(UPDATED_CODE).imgUrl(UPDATED_IMG_URL).notes(UPDATED_NOTES);

        restDocTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocTopic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocTopic))
            )
            .andExpect(status().isOk());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
        DocTopic testDocTopic = docTopicList.get(docTopicList.size() - 1);
        assertThat(testDocTopic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocTopic.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDocTopic.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testDocTopic.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingDocTopic() throws Exception {
        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();
        docTopic.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docTopic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docTopic))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocTopic() throws Exception {
        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();
        docTopic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTopicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docTopic))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocTopic() throws Exception {
        int databaseSizeBeforeUpdate = docTopicRepository.findAll().size();
        docTopic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocTopicMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docTopic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocTopic in the database
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocTopic() throws Exception {
        // Initialize the database
        docTopicRepository.saveAndFlush(docTopic);

        int databaseSizeBeforeDelete = docTopicRepository.findAll().size();

        // Delete the docTopic
        restDocTopicMockMvc
            .perform(delete(ENTITY_API_URL_ID, docTopic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocTopic> docTopicList = docTopicRepository.findAll();
        assertThat(docTopicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
