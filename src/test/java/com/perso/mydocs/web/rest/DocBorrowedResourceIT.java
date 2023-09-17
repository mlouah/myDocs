package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.domain.DocBorrowed;
import com.perso.mydocs.repository.DocBorrowedRepository;
import com.perso.mydocs.service.DocBorrowedService;
import com.perso.mydocs.service.criteria.DocBorrowedCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link DocBorrowedResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocBorrowedResourceIT {

    private static final LocalDate DEFAULT_BORROW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BORROW_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BORROW_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BORROWER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BORROWER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doc-borroweds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocBorrowedRepository docBorrowedRepository;

    @Mock
    private DocBorrowedRepository docBorrowedRepositoryMock;

    @Mock
    private DocBorrowedService docBorrowedServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocBorrowedMockMvc;

    private DocBorrowed docBorrowed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocBorrowed createEntity(EntityManager em) {
        DocBorrowed docBorrowed = new DocBorrowed()
            .borrowDate(DEFAULT_BORROW_DATE)
            .borrowerName(DEFAULT_BORROWER_NAME)
            .notes(DEFAULT_NOTES);
        return docBorrowed;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocBorrowed createUpdatedEntity(EntityManager em) {
        DocBorrowed docBorrowed = new DocBorrowed()
            .borrowDate(UPDATED_BORROW_DATE)
            .borrowerName(UPDATED_BORROWER_NAME)
            .notes(UPDATED_NOTES);
        return docBorrowed;
    }

    @BeforeEach
    public void initTest() {
        docBorrowed = createEntity(em);
    }

    @Test
    @Transactional
    void createDocBorrowed() throws Exception {
        int databaseSizeBeforeCreate = docBorrowedRepository.findAll().size();
        // Create the DocBorrowed
        restDocBorrowedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docBorrowed)))
            .andExpect(status().isCreated());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeCreate + 1);
        DocBorrowed testDocBorrowed = docBorrowedList.get(docBorrowedList.size() - 1);
        assertThat(testDocBorrowed.getBorrowDate()).isEqualTo(DEFAULT_BORROW_DATE);
        assertThat(testDocBorrowed.getBorrowerName()).isEqualTo(DEFAULT_BORROWER_NAME);
        assertThat(testDocBorrowed.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createDocBorrowedWithExistingId() throws Exception {
        // Create the DocBorrowed with an existing ID
        docBorrowed.setId(1L);

        int databaseSizeBeforeCreate = docBorrowedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocBorrowedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docBorrowed)))
            .andExpect(status().isBadRequest());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocBorroweds() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList
        restDocBorrowedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docBorrowed.getId().intValue())))
            .andExpect(jsonPath("$.[*].borrowDate").value(hasItem(DEFAULT_BORROW_DATE.toString())))
            .andExpect(jsonPath("$.[*].borrowerName").value(hasItem(DEFAULT_BORROWER_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocBorrowedsWithEagerRelationshipsIsEnabled() throws Exception {
        when(docBorrowedServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocBorrowedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(docBorrowedServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocBorrowedsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(docBorrowedServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocBorrowedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(docBorrowedRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocBorrowed() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get the docBorrowed
        restDocBorrowedMockMvc
            .perform(get(ENTITY_API_URL_ID, docBorrowed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(docBorrowed.getId().intValue()))
            .andExpect(jsonPath("$.borrowDate").value(DEFAULT_BORROW_DATE.toString()))
            .andExpect(jsonPath("$.borrowerName").value(DEFAULT_BORROWER_NAME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getDocBorrowedsByIdFiltering() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        Long id = docBorrowed.getId();

        defaultDocBorrowedShouldBeFound("id.equals=" + id);
        defaultDocBorrowedShouldNotBeFound("id.notEquals=" + id);

        defaultDocBorrowedShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocBorrowedShouldNotBeFound("id.greaterThan=" + id);

        defaultDocBorrowedShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocBorrowedShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowDateIsEqualToSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowDate equals to DEFAULT_BORROW_DATE
        defaultDocBorrowedShouldBeFound("borrowDate.equals=" + DEFAULT_BORROW_DATE);

        // Get all the docBorrowedList where borrowDate equals to UPDATED_BORROW_DATE
        defaultDocBorrowedShouldNotBeFound("borrowDate.equals=" + UPDATED_BORROW_DATE);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowDateIsInShouldWork() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowDate in DEFAULT_BORROW_DATE or UPDATED_BORROW_DATE
        defaultDocBorrowedShouldBeFound("borrowDate.in=" + DEFAULT_BORROW_DATE + "," + UPDATED_BORROW_DATE);

        // Get all the docBorrowedList where borrowDate equals to UPDATED_BORROW_DATE
        defaultDocBorrowedShouldNotBeFound("borrowDate.in=" + UPDATED_BORROW_DATE);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowDate is not null
        defaultDocBorrowedShouldBeFound("borrowDate.specified=true");

        // Get all the docBorrowedList where borrowDate is null
        defaultDocBorrowedShouldNotBeFound("borrowDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowDate is greater than or equal to DEFAULT_BORROW_DATE
        defaultDocBorrowedShouldBeFound("borrowDate.greaterThanOrEqual=" + DEFAULT_BORROW_DATE);

        // Get all the docBorrowedList where borrowDate is greater than or equal to UPDATED_BORROW_DATE
        defaultDocBorrowedShouldNotBeFound("borrowDate.greaterThanOrEqual=" + UPDATED_BORROW_DATE);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowDate is less than or equal to DEFAULT_BORROW_DATE
        defaultDocBorrowedShouldBeFound("borrowDate.lessThanOrEqual=" + DEFAULT_BORROW_DATE);

        // Get all the docBorrowedList where borrowDate is less than or equal to SMALLER_BORROW_DATE
        defaultDocBorrowedShouldNotBeFound("borrowDate.lessThanOrEqual=" + SMALLER_BORROW_DATE);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowDateIsLessThanSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowDate is less than DEFAULT_BORROW_DATE
        defaultDocBorrowedShouldNotBeFound("borrowDate.lessThan=" + DEFAULT_BORROW_DATE);

        // Get all the docBorrowedList where borrowDate is less than UPDATED_BORROW_DATE
        defaultDocBorrowedShouldBeFound("borrowDate.lessThan=" + UPDATED_BORROW_DATE);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowDate is greater than DEFAULT_BORROW_DATE
        defaultDocBorrowedShouldNotBeFound("borrowDate.greaterThan=" + DEFAULT_BORROW_DATE);

        // Get all the docBorrowedList where borrowDate is greater than SMALLER_BORROW_DATE
        defaultDocBorrowedShouldBeFound("borrowDate.greaterThan=" + SMALLER_BORROW_DATE);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowerName equals to DEFAULT_BORROWER_NAME
        defaultDocBorrowedShouldBeFound("borrowerName.equals=" + DEFAULT_BORROWER_NAME);

        // Get all the docBorrowedList where borrowerName equals to UPDATED_BORROWER_NAME
        defaultDocBorrowedShouldNotBeFound("borrowerName.equals=" + UPDATED_BORROWER_NAME);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowerNameIsInShouldWork() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowerName in DEFAULT_BORROWER_NAME or UPDATED_BORROWER_NAME
        defaultDocBorrowedShouldBeFound("borrowerName.in=" + DEFAULT_BORROWER_NAME + "," + UPDATED_BORROWER_NAME);

        // Get all the docBorrowedList where borrowerName equals to UPDATED_BORROWER_NAME
        defaultDocBorrowedShouldNotBeFound("borrowerName.in=" + UPDATED_BORROWER_NAME);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowerName is not null
        defaultDocBorrowedShouldBeFound("borrowerName.specified=true");

        // Get all the docBorrowedList where borrowerName is null
        defaultDocBorrowedShouldNotBeFound("borrowerName.specified=false");
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowerNameContainsSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowerName contains DEFAULT_BORROWER_NAME
        defaultDocBorrowedShouldBeFound("borrowerName.contains=" + DEFAULT_BORROWER_NAME);

        // Get all the docBorrowedList where borrowerName contains UPDATED_BORROWER_NAME
        defaultDocBorrowedShouldNotBeFound("borrowerName.contains=" + UPDATED_BORROWER_NAME);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByBorrowerNameNotContainsSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where borrowerName does not contain DEFAULT_BORROWER_NAME
        defaultDocBorrowedShouldNotBeFound("borrowerName.doesNotContain=" + DEFAULT_BORROWER_NAME);

        // Get all the docBorrowedList where borrowerName does not contain UPDATED_BORROWER_NAME
        defaultDocBorrowedShouldBeFound("borrowerName.doesNotContain=" + UPDATED_BORROWER_NAME);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where notes equals to DEFAULT_NOTES
        defaultDocBorrowedShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the docBorrowedList where notes equals to UPDATED_NOTES
        defaultDocBorrowedShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultDocBorrowedShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the docBorrowedList where notes equals to UPDATED_NOTES
        defaultDocBorrowedShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where notes is not null
        defaultDocBorrowedShouldBeFound("notes.specified=true");

        // Get all the docBorrowedList where notes is null
        defaultDocBorrowedShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByNotesContainsSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where notes contains DEFAULT_NOTES
        defaultDocBorrowedShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the docBorrowedList where notes contains UPDATED_NOTES
        defaultDocBorrowedShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        // Get all the docBorrowedList where notes does not contain DEFAULT_NOTES
        defaultDocBorrowedShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the docBorrowedList where notes does not contain UPDATED_NOTES
        defaultDocBorrowedShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllDocBorrowedsByDocIsEqualToSomething() throws Exception {
        Doc doc;
        if (TestUtil.findAll(em, Doc.class).isEmpty()) {
            docBorrowedRepository.saveAndFlush(docBorrowed);
            doc = DocResourceIT.createEntity(em);
        } else {
            doc = TestUtil.findAll(em, Doc.class).get(0);
        }
        em.persist(doc);
        em.flush();
        docBorrowed.setDoc(doc);
        docBorrowedRepository.saveAndFlush(docBorrowed);
        Long docId = doc.getId();

        // Get all the docBorrowedList where doc equals to docId
        defaultDocBorrowedShouldBeFound("docId.equals=" + docId);

        // Get all the docBorrowedList where doc equals to (docId + 1)
        defaultDocBorrowedShouldNotBeFound("docId.equals=" + (docId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocBorrowedShouldBeFound(String filter) throws Exception {
        restDocBorrowedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docBorrowed.getId().intValue())))
            .andExpect(jsonPath("$.[*].borrowDate").value(hasItem(DEFAULT_BORROW_DATE.toString())))
            .andExpect(jsonPath("$.[*].borrowerName").value(hasItem(DEFAULT_BORROWER_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restDocBorrowedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocBorrowedShouldNotBeFound(String filter) throws Exception {
        restDocBorrowedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocBorrowedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocBorrowed() throws Exception {
        // Get the docBorrowed
        restDocBorrowedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocBorrowed() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();

        // Update the docBorrowed
        DocBorrowed updatedDocBorrowed = docBorrowedRepository.findById(docBorrowed.getId()).get();
        // Disconnect from session so that the updates on updatedDocBorrowed are not directly saved in db
        em.detach(updatedDocBorrowed);
        updatedDocBorrowed.borrowDate(UPDATED_BORROW_DATE).borrowerName(UPDATED_BORROWER_NAME).notes(UPDATED_NOTES);

        restDocBorrowedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocBorrowed.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocBorrowed))
            )
            .andExpect(status().isOk());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
        DocBorrowed testDocBorrowed = docBorrowedList.get(docBorrowedList.size() - 1);
        assertThat(testDocBorrowed.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testDocBorrowed.getBorrowerName()).isEqualTo(UPDATED_BORROWER_NAME);
        assertThat(testDocBorrowed.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingDocBorrowed() throws Exception {
        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();
        docBorrowed.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocBorrowedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, docBorrowed.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docBorrowed))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocBorrowed() throws Exception {
        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();
        docBorrowed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocBorrowedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(docBorrowed))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocBorrowed() throws Exception {
        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();
        docBorrowed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocBorrowedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(docBorrowed)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocBorrowedWithPatch() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();

        // Update the docBorrowed using partial update
        DocBorrowed partialUpdatedDocBorrowed = new DocBorrowed();
        partialUpdatedDocBorrowed.setId(docBorrowed.getId());

        partialUpdatedDocBorrowed.borrowDate(UPDATED_BORROW_DATE).borrowerName(UPDATED_BORROWER_NAME).notes(UPDATED_NOTES);

        restDocBorrowedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocBorrowed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocBorrowed))
            )
            .andExpect(status().isOk());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
        DocBorrowed testDocBorrowed = docBorrowedList.get(docBorrowedList.size() - 1);
        assertThat(testDocBorrowed.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testDocBorrowed.getBorrowerName()).isEqualTo(UPDATED_BORROWER_NAME);
        assertThat(testDocBorrowed.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateDocBorrowedWithPatch() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();

        // Update the docBorrowed using partial update
        DocBorrowed partialUpdatedDocBorrowed = new DocBorrowed();
        partialUpdatedDocBorrowed.setId(docBorrowed.getId());

        partialUpdatedDocBorrowed.borrowDate(UPDATED_BORROW_DATE).borrowerName(UPDATED_BORROWER_NAME).notes(UPDATED_NOTES);

        restDocBorrowedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocBorrowed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocBorrowed))
            )
            .andExpect(status().isOk());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
        DocBorrowed testDocBorrowed = docBorrowedList.get(docBorrowedList.size() - 1);
        assertThat(testDocBorrowed.getBorrowDate()).isEqualTo(UPDATED_BORROW_DATE);
        assertThat(testDocBorrowed.getBorrowerName()).isEqualTo(UPDATED_BORROWER_NAME);
        assertThat(testDocBorrowed.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingDocBorrowed() throws Exception {
        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();
        docBorrowed.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocBorrowedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, docBorrowed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docBorrowed))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocBorrowed() throws Exception {
        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();
        docBorrowed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocBorrowedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(docBorrowed))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocBorrowed() throws Exception {
        int databaseSizeBeforeUpdate = docBorrowedRepository.findAll().size();
        docBorrowed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocBorrowedMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(docBorrowed))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocBorrowed in the database
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocBorrowed() throws Exception {
        // Initialize the database
        docBorrowedRepository.saveAndFlush(docBorrowed);

        int databaseSizeBeforeDelete = docBorrowedRepository.findAll().size();

        // Delete the docBorrowed
        restDocBorrowedMockMvc
            .perform(delete(ENTITY_API_URL_ID, docBorrowed.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocBorrowed> docBorrowedList = docBorrowedRepository.findAll();
        assertThat(docBorrowedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
