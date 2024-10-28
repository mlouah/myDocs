package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.domain.DocAuthor;
import com.perso.mydocs.domain.DocCategory;
import com.perso.mydocs.domain.DocCollection;
import com.perso.mydocs.domain.DocFormat;
import com.perso.mydocs.domain.DocPublisher;
import com.perso.mydocs.domain.DocTopic;
import com.perso.mydocs.domain.Language;
import com.perso.mydocs.repository.DocRepository;
import com.perso.mydocs.service.DocService;
import com.perso.mydocs.service.criteria.DocCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISH_YEAR = "AAAA";
    private static final String UPDATED_PUBLISH_YEAR = "BBBB";

    private static final String DEFAULT_COVER_IMG_PATH = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMG_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDITION_NUMER = 1;
    private static final Integer UPDATED_EDITION_NUMER = 2;
    private static final Integer SMALLER_EDITION_NUMER = 1 - 1;

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_START_READING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_READING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_READING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_READING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_READING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_READING_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final Integer DEFAULT_COPIES = 1;
    private static final Integer UPDATED_COPIES = 2;
    private static final Integer SMALLER_COPIES = 1 - 1;

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    private static final String DEFAULT_NUM_DOC = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOC = "BBBBBBBBBB";

    private static final String DEFAULT_MY_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_MY_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final String DEFAULT_TOC = "AAAAAAAAAA";
    private static final String UPDATED_TOC = "BBBBBBBBBB";

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/docs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocRepository docRepository;

    @Mock
    private DocRepository docRepositoryMock;

    @Mock
    private DocService docServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocMockMvc;

    private Doc doc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doc createEntity(EntityManager em) {
        Doc doc = new Doc()
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .publishYear(DEFAULT_PUBLISH_YEAR)
            .coverImgPath(DEFAULT_COVER_IMG_PATH)
            .editionNumer(DEFAULT_EDITION_NUMER)
            .summary(DEFAULT_SUMMARY)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .startReadingDate(DEFAULT_START_READING_DATE)
            .endReadingDate(DEFAULT_END_READING_DATE)
            .price(DEFAULT_PRICE)
            .copies(DEFAULT_COPIES)
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .numDoc(DEFAULT_NUM_DOC)
            .myNotes(DEFAULT_MY_NOTES)
            .keywords(DEFAULT_KEYWORDS)
            .toc(DEFAULT_TOC)
            .filename(DEFAULT_FILENAME);
        return doc;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doc createUpdatedEntity(EntityManager em) {
        Doc doc = new Doc()
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .publishYear(UPDATED_PUBLISH_YEAR)
            .coverImgPath(UPDATED_COVER_IMG_PATH)
            .editionNumer(UPDATED_EDITION_NUMER)
            .summary(UPDATED_SUMMARY)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .startReadingDate(UPDATED_START_READING_DATE)
            .endReadingDate(UPDATED_END_READING_DATE)
            .price(UPDATED_PRICE)
            .copies(UPDATED_COPIES)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .numDoc(UPDATED_NUM_DOC)
            .myNotes(UPDATED_MY_NOTES)
            .keywords(UPDATED_KEYWORDS)
            .toc(UPDATED_TOC)
            .filename(UPDATED_FILENAME);
        return doc;
    }

    @BeforeEach
    public void initTest() {
        doc = createEntity(em);
    }

    @Test
    @Transactional
    void createDoc() throws Exception {
        int databaseSizeBeforeCreate = docRepository.findAll().size();
        // Create the Doc
        restDocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doc)))
            .andExpect(status().isCreated());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeCreate + 1);
        Doc testDoc = docList.get(docList.size() - 1);
        assertThat(testDoc.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDoc.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testDoc.getPublishYear()).isEqualTo(DEFAULT_PUBLISH_YEAR);
        assertThat(testDoc.getCoverImgPath()).isEqualTo(DEFAULT_COVER_IMG_PATH);
        assertThat(testDoc.getEditionNumer()).isEqualTo(DEFAULT_EDITION_NUMER);
        assertThat(testDoc.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testDoc.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testDoc.getStartReadingDate()).isEqualTo(DEFAULT_START_READING_DATE);
        assertThat(testDoc.getEndReadingDate()).isEqualTo(DEFAULT_END_READING_DATE);
        assertThat(testDoc.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testDoc.getCopies()).isEqualTo(DEFAULT_COPIES);
        assertThat(testDoc.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(testDoc.getNumDoc()).isEqualTo(DEFAULT_NUM_DOC);
        assertThat(testDoc.getMyNotes()).isEqualTo(DEFAULT_MY_NOTES);
        assertThat(testDoc.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testDoc.getToc()).isEqualTo(DEFAULT_TOC);
        assertThat(testDoc.getFilename()).isEqualTo(DEFAULT_FILENAME);
    }

    @Test
    @Transactional
    void createDocWithExistingId() throws Exception {
        // Create the Doc with an existing ID
        doc.setId(1L);

        int databaseSizeBeforeCreate = docRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doc)))
            .andExpect(status().isBadRequest());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = docRepository.findAll().size();
        // set the field null
        doc.setTitle(null);

        // Create the Doc, which fails.

        restDocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doc)))
            .andExpect(status().isBadRequest());

        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocs() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList
        restDocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doc.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].publishYear").value(hasItem(DEFAULT_PUBLISH_YEAR)))
            .andExpect(jsonPath("$.[*].coverImgPath").value(hasItem(DEFAULT_COVER_IMG_PATH)))
            .andExpect(jsonPath("$.[*].editionNumer").value(hasItem(DEFAULT_EDITION_NUMER)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].startReadingDate").value(hasItem(DEFAULT_START_READING_DATE.toString())))
            .andExpect(jsonPath("$.[*].endReadingDate").value(hasItem(DEFAULT_END_READING_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].copies").value(hasItem(DEFAULT_COPIES)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].numDoc").value(hasItem(DEFAULT_NUM_DOC)))
            .andExpect(jsonPath("$.[*].myNotes").value(hasItem(DEFAULT_MY_NOTES.toString())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].toc").value(hasItem(DEFAULT_TOC.toString())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocsWithEagerRelationshipsIsEnabled() throws Exception {
        when(docServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(docServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(docServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(docRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDoc() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get the doc
        restDocMockMvc
            .perform(get(ENTITY_API_URL_ID, doc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doc.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.publishYear").value(DEFAULT_PUBLISH_YEAR))
            .andExpect(jsonPath("$.coverImgPath").value(DEFAULT_COVER_IMG_PATH))
            .andExpect(jsonPath("$.editionNumer").value(DEFAULT_EDITION_NUMER))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.startReadingDate").value(DEFAULT_START_READING_DATE.toString()))
            .andExpect(jsonPath("$.endReadingDate").value(DEFAULT_END_READING_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.copies").value(DEFAULT_COPIES))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.numDoc").value(DEFAULT_NUM_DOC))
            .andExpect(jsonPath("$.myNotes").value(DEFAULT_MY_NOTES.toString()))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS.toString()))
            .andExpect(jsonPath("$.toc").value(DEFAULT_TOC.toString()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME));
    }

    @Test
    @Transactional
    void getDocsByIdFiltering() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        Long id = doc.getId();

        defaultDocShouldBeFound("id.equals=" + id);
        defaultDocShouldNotBeFound("id.notEquals=" + id);

        defaultDocShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocShouldNotBeFound("id.greaterThan=" + id);

        defaultDocShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where title equals to DEFAULT_TITLE
        defaultDocShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the docList where title equals to UPDATED_TITLE
        defaultDocShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultDocShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the docList where title equals to UPDATED_TITLE
        defaultDocShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where title is not null
        defaultDocShouldBeFound("title.specified=true");

        // Get all the docList where title is null
        defaultDocShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByTitleContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where title contains DEFAULT_TITLE
        defaultDocShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the docList where title contains UPDATED_TITLE
        defaultDocShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where title does not contain DEFAULT_TITLE
        defaultDocShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the docList where title does not contain UPDATED_TITLE
        defaultDocShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsBySubTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where subTitle equals to DEFAULT_SUB_TITLE
        defaultDocShouldBeFound("subTitle.equals=" + DEFAULT_SUB_TITLE);

        // Get all the docList where subTitle equals to UPDATED_SUB_TITLE
        defaultDocShouldNotBeFound("subTitle.equals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsBySubTitleIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where subTitle in DEFAULT_SUB_TITLE or UPDATED_SUB_TITLE
        defaultDocShouldBeFound("subTitle.in=" + DEFAULT_SUB_TITLE + "," + UPDATED_SUB_TITLE);

        // Get all the docList where subTitle equals to UPDATED_SUB_TITLE
        defaultDocShouldNotBeFound("subTitle.in=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsBySubTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where subTitle is not null
        defaultDocShouldBeFound("subTitle.specified=true");

        // Get all the docList where subTitle is null
        defaultDocShouldNotBeFound("subTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsBySubTitleContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where subTitle contains DEFAULT_SUB_TITLE
        defaultDocShouldBeFound("subTitle.contains=" + DEFAULT_SUB_TITLE);

        // Get all the docList where subTitle contains UPDATED_SUB_TITLE
        defaultDocShouldNotBeFound("subTitle.contains=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsBySubTitleNotContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where subTitle does not contain DEFAULT_SUB_TITLE
        defaultDocShouldNotBeFound("subTitle.doesNotContain=" + DEFAULT_SUB_TITLE);

        // Get all the docList where subTitle does not contain UPDATED_SUB_TITLE
        defaultDocShouldBeFound("subTitle.doesNotContain=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllDocsByPublishYearIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where publishYear equals to DEFAULT_PUBLISH_YEAR
        defaultDocShouldBeFound("publishYear.equals=" + DEFAULT_PUBLISH_YEAR);

        // Get all the docList where publishYear equals to UPDATED_PUBLISH_YEAR
        defaultDocShouldNotBeFound("publishYear.equals=" + UPDATED_PUBLISH_YEAR);
    }

    @Test
    @Transactional
    void getAllDocsByPublishYearIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where publishYear in DEFAULT_PUBLISH_YEAR or UPDATED_PUBLISH_YEAR
        defaultDocShouldBeFound("publishYear.in=" + DEFAULT_PUBLISH_YEAR + "," + UPDATED_PUBLISH_YEAR);

        // Get all the docList where publishYear equals to UPDATED_PUBLISH_YEAR
        defaultDocShouldNotBeFound("publishYear.in=" + UPDATED_PUBLISH_YEAR);
    }

    @Test
    @Transactional
    void getAllDocsByPublishYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where publishYear is not null
        defaultDocShouldBeFound("publishYear.specified=true");

        // Get all the docList where publishYear is null
        defaultDocShouldNotBeFound("publishYear.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByPublishYearContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where publishYear contains DEFAULT_PUBLISH_YEAR
        defaultDocShouldBeFound("publishYear.contains=" + DEFAULT_PUBLISH_YEAR);

        // Get all the docList where publishYear contains UPDATED_PUBLISH_YEAR
        defaultDocShouldNotBeFound("publishYear.contains=" + UPDATED_PUBLISH_YEAR);
    }

    @Test
    @Transactional
    void getAllDocsByPublishYearNotContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where publishYear does not contain DEFAULT_PUBLISH_YEAR
        defaultDocShouldNotBeFound("publishYear.doesNotContain=" + DEFAULT_PUBLISH_YEAR);

        // Get all the docList where publishYear does not contain UPDATED_PUBLISH_YEAR
        defaultDocShouldBeFound("publishYear.doesNotContain=" + UPDATED_PUBLISH_YEAR);
    }

    @Test
    @Transactional
    void getAllDocsByCoverImgPathIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where coverImgPath equals to DEFAULT_COVER_IMG_PATH
        defaultDocShouldBeFound("coverImgPath.equals=" + DEFAULT_COVER_IMG_PATH);

        // Get all the docList where coverImgPath equals to UPDATED_COVER_IMG_PATH
        defaultDocShouldNotBeFound("coverImgPath.equals=" + UPDATED_COVER_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllDocsByCoverImgPathIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where coverImgPath in DEFAULT_COVER_IMG_PATH or UPDATED_COVER_IMG_PATH
        defaultDocShouldBeFound("coverImgPath.in=" + DEFAULT_COVER_IMG_PATH + "," + UPDATED_COVER_IMG_PATH);

        // Get all the docList where coverImgPath equals to UPDATED_COVER_IMG_PATH
        defaultDocShouldNotBeFound("coverImgPath.in=" + UPDATED_COVER_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllDocsByCoverImgPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where coverImgPath is not null
        defaultDocShouldBeFound("coverImgPath.specified=true");

        // Get all the docList where coverImgPath is null
        defaultDocShouldNotBeFound("coverImgPath.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByCoverImgPathContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where coverImgPath contains DEFAULT_COVER_IMG_PATH
        defaultDocShouldBeFound("coverImgPath.contains=" + DEFAULT_COVER_IMG_PATH);

        // Get all the docList where coverImgPath contains UPDATED_COVER_IMG_PATH
        defaultDocShouldNotBeFound("coverImgPath.contains=" + UPDATED_COVER_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllDocsByCoverImgPathNotContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where coverImgPath does not contain DEFAULT_COVER_IMG_PATH
        defaultDocShouldNotBeFound("coverImgPath.doesNotContain=" + DEFAULT_COVER_IMG_PATH);

        // Get all the docList where coverImgPath does not contain UPDATED_COVER_IMG_PATH
        defaultDocShouldBeFound("coverImgPath.doesNotContain=" + UPDATED_COVER_IMG_PATH);
    }

    @Test
    @Transactional
    void getAllDocsByEditionNumerIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where editionNumer equals to DEFAULT_EDITION_NUMER
        defaultDocShouldBeFound("editionNumer.equals=" + DEFAULT_EDITION_NUMER);

        // Get all the docList where editionNumer equals to UPDATED_EDITION_NUMER
        defaultDocShouldNotBeFound("editionNumer.equals=" + UPDATED_EDITION_NUMER);
    }

    @Test
    @Transactional
    void getAllDocsByEditionNumerIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where editionNumer in DEFAULT_EDITION_NUMER or UPDATED_EDITION_NUMER
        defaultDocShouldBeFound("editionNumer.in=" + DEFAULT_EDITION_NUMER + "," + UPDATED_EDITION_NUMER);

        // Get all the docList where editionNumer equals to UPDATED_EDITION_NUMER
        defaultDocShouldNotBeFound("editionNumer.in=" + UPDATED_EDITION_NUMER);
    }

    @Test
    @Transactional
    void getAllDocsByEditionNumerIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where editionNumer is not null
        defaultDocShouldBeFound("editionNumer.specified=true");

        // Get all the docList where editionNumer is null
        defaultDocShouldNotBeFound("editionNumer.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByEditionNumerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where editionNumer is greater than or equal to DEFAULT_EDITION_NUMER
        defaultDocShouldBeFound("editionNumer.greaterThanOrEqual=" + DEFAULT_EDITION_NUMER);

        // Get all the docList where editionNumer is greater than or equal to UPDATED_EDITION_NUMER
        defaultDocShouldNotBeFound("editionNumer.greaterThanOrEqual=" + UPDATED_EDITION_NUMER);
    }

    @Test
    @Transactional
    void getAllDocsByEditionNumerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where editionNumer is less than or equal to DEFAULT_EDITION_NUMER
        defaultDocShouldBeFound("editionNumer.lessThanOrEqual=" + DEFAULT_EDITION_NUMER);

        // Get all the docList where editionNumer is less than or equal to SMALLER_EDITION_NUMER
        defaultDocShouldNotBeFound("editionNumer.lessThanOrEqual=" + SMALLER_EDITION_NUMER);
    }

    @Test
    @Transactional
    void getAllDocsByEditionNumerIsLessThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where editionNumer is less than DEFAULT_EDITION_NUMER
        defaultDocShouldNotBeFound("editionNumer.lessThan=" + DEFAULT_EDITION_NUMER);

        // Get all the docList where editionNumer is less than UPDATED_EDITION_NUMER
        defaultDocShouldBeFound("editionNumer.lessThan=" + UPDATED_EDITION_NUMER);
    }

    @Test
    @Transactional
    void getAllDocsByEditionNumerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where editionNumer is greater than DEFAULT_EDITION_NUMER
        defaultDocShouldNotBeFound("editionNumer.greaterThan=" + DEFAULT_EDITION_NUMER);

        // Get all the docList where editionNumer is greater than SMALLER_EDITION_NUMER
        defaultDocShouldBeFound("editionNumer.greaterThan=" + SMALLER_EDITION_NUMER);
    }

    @Test
    @Transactional
    void getAllDocsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultDocShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the docList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultDocShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultDocShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the docList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultDocShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where purchaseDate is not null
        defaultDocShouldBeFound("purchaseDate.specified=true");

        // Get all the docList where purchaseDate is null
        defaultDocShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where purchaseDate is greater than or equal to DEFAULT_PURCHASE_DATE
        defaultDocShouldBeFound("purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the docList where purchaseDate is greater than or equal to UPDATED_PURCHASE_DATE
        defaultDocShouldNotBeFound("purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where purchaseDate is less than or equal to DEFAULT_PURCHASE_DATE
        defaultDocShouldBeFound("purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the docList where purchaseDate is less than or equal to SMALLER_PURCHASE_DATE
        defaultDocShouldNotBeFound("purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where purchaseDate is less than DEFAULT_PURCHASE_DATE
        defaultDocShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the docList where purchaseDate is less than UPDATED_PURCHASE_DATE
        defaultDocShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where purchaseDate is greater than DEFAULT_PURCHASE_DATE
        defaultDocShouldNotBeFound("purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the docList where purchaseDate is greater than SMALLER_PURCHASE_DATE
        defaultDocShouldBeFound("purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByStartReadingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where startReadingDate equals to DEFAULT_START_READING_DATE
        defaultDocShouldBeFound("startReadingDate.equals=" + DEFAULT_START_READING_DATE);

        // Get all the docList where startReadingDate equals to UPDATED_START_READING_DATE
        defaultDocShouldNotBeFound("startReadingDate.equals=" + UPDATED_START_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByStartReadingDateIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where startReadingDate in DEFAULT_START_READING_DATE or UPDATED_START_READING_DATE
        defaultDocShouldBeFound("startReadingDate.in=" + DEFAULT_START_READING_DATE + "," + UPDATED_START_READING_DATE);

        // Get all the docList where startReadingDate equals to UPDATED_START_READING_DATE
        defaultDocShouldNotBeFound("startReadingDate.in=" + UPDATED_START_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByStartReadingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where startReadingDate is not null
        defaultDocShouldBeFound("startReadingDate.specified=true");

        // Get all the docList where startReadingDate is null
        defaultDocShouldNotBeFound("startReadingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByStartReadingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where startReadingDate is greater than or equal to DEFAULT_START_READING_DATE
        defaultDocShouldBeFound("startReadingDate.greaterThanOrEqual=" + DEFAULT_START_READING_DATE);

        // Get all the docList where startReadingDate is greater than or equal to UPDATED_START_READING_DATE
        defaultDocShouldNotBeFound("startReadingDate.greaterThanOrEqual=" + UPDATED_START_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByStartReadingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where startReadingDate is less than or equal to DEFAULT_START_READING_DATE
        defaultDocShouldBeFound("startReadingDate.lessThanOrEqual=" + DEFAULT_START_READING_DATE);

        // Get all the docList where startReadingDate is less than or equal to SMALLER_START_READING_DATE
        defaultDocShouldNotBeFound("startReadingDate.lessThanOrEqual=" + SMALLER_START_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByStartReadingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where startReadingDate is less than DEFAULT_START_READING_DATE
        defaultDocShouldNotBeFound("startReadingDate.lessThan=" + DEFAULT_START_READING_DATE);

        // Get all the docList where startReadingDate is less than UPDATED_START_READING_DATE
        defaultDocShouldBeFound("startReadingDate.lessThan=" + UPDATED_START_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByStartReadingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where startReadingDate is greater than DEFAULT_START_READING_DATE
        defaultDocShouldNotBeFound("startReadingDate.greaterThan=" + DEFAULT_START_READING_DATE);

        // Get all the docList where startReadingDate is greater than SMALLER_START_READING_DATE
        defaultDocShouldBeFound("startReadingDate.greaterThan=" + SMALLER_START_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByEndReadingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where endReadingDate equals to DEFAULT_END_READING_DATE
        defaultDocShouldBeFound("endReadingDate.equals=" + DEFAULT_END_READING_DATE);

        // Get all the docList where endReadingDate equals to UPDATED_END_READING_DATE
        defaultDocShouldNotBeFound("endReadingDate.equals=" + UPDATED_END_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByEndReadingDateIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where endReadingDate in DEFAULT_END_READING_DATE or UPDATED_END_READING_DATE
        defaultDocShouldBeFound("endReadingDate.in=" + DEFAULT_END_READING_DATE + "," + UPDATED_END_READING_DATE);

        // Get all the docList where endReadingDate equals to UPDATED_END_READING_DATE
        defaultDocShouldNotBeFound("endReadingDate.in=" + UPDATED_END_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByEndReadingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where endReadingDate is not null
        defaultDocShouldBeFound("endReadingDate.specified=true");

        // Get all the docList where endReadingDate is null
        defaultDocShouldNotBeFound("endReadingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByEndReadingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where endReadingDate is greater than or equal to DEFAULT_END_READING_DATE
        defaultDocShouldBeFound("endReadingDate.greaterThanOrEqual=" + DEFAULT_END_READING_DATE);

        // Get all the docList where endReadingDate is greater than or equal to UPDATED_END_READING_DATE
        defaultDocShouldNotBeFound("endReadingDate.greaterThanOrEqual=" + UPDATED_END_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByEndReadingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where endReadingDate is less than or equal to DEFAULT_END_READING_DATE
        defaultDocShouldBeFound("endReadingDate.lessThanOrEqual=" + DEFAULT_END_READING_DATE);

        // Get all the docList where endReadingDate is less than or equal to SMALLER_END_READING_DATE
        defaultDocShouldNotBeFound("endReadingDate.lessThanOrEqual=" + SMALLER_END_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByEndReadingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where endReadingDate is less than DEFAULT_END_READING_DATE
        defaultDocShouldNotBeFound("endReadingDate.lessThan=" + DEFAULT_END_READING_DATE);

        // Get all the docList where endReadingDate is less than UPDATED_END_READING_DATE
        defaultDocShouldBeFound("endReadingDate.lessThan=" + UPDATED_END_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByEndReadingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where endReadingDate is greater than DEFAULT_END_READING_DATE
        defaultDocShouldNotBeFound("endReadingDate.greaterThan=" + DEFAULT_END_READING_DATE);

        // Get all the docList where endReadingDate is greater than SMALLER_END_READING_DATE
        defaultDocShouldBeFound("endReadingDate.greaterThan=" + SMALLER_END_READING_DATE);
    }

    @Test
    @Transactional
    void getAllDocsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where price equals to DEFAULT_PRICE
        defaultDocShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the docList where price equals to UPDATED_PRICE
        defaultDocShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDocsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultDocShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the docList where price equals to UPDATED_PRICE
        defaultDocShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDocsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where price is not null
        defaultDocShouldBeFound("price.specified=true");

        // Get all the docList where price is null
        defaultDocShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where price is greater than or equal to DEFAULT_PRICE
        defaultDocShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the docList where price is greater than or equal to UPDATED_PRICE
        defaultDocShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDocsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where price is less than or equal to DEFAULT_PRICE
        defaultDocShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the docList where price is less than or equal to SMALLER_PRICE
        defaultDocShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllDocsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where price is less than DEFAULT_PRICE
        defaultDocShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the docList where price is less than UPDATED_PRICE
        defaultDocShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDocsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where price is greater than DEFAULT_PRICE
        defaultDocShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the docList where price is greater than SMALLER_PRICE
        defaultDocShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllDocsByCopiesIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where copies equals to DEFAULT_COPIES
        defaultDocShouldBeFound("copies.equals=" + DEFAULT_COPIES);

        // Get all the docList where copies equals to UPDATED_COPIES
        defaultDocShouldNotBeFound("copies.equals=" + UPDATED_COPIES);
    }

    @Test
    @Transactional
    void getAllDocsByCopiesIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where copies in DEFAULT_COPIES or UPDATED_COPIES
        defaultDocShouldBeFound("copies.in=" + DEFAULT_COPIES + "," + UPDATED_COPIES);

        // Get all the docList where copies equals to UPDATED_COPIES
        defaultDocShouldNotBeFound("copies.in=" + UPDATED_COPIES);
    }

    @Test
    @Transactional
    void getAllDocsByCopiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where copies is not null
        defaultDocShouldBeFound("copies.specified=true");

        // Get all the docList where copies is null
        defaultDocShouldNotBeFound("copies.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByCopiesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where copies is greater than or equal to DEFAULT_COPIES
        defaultDocShouldBeFound("copies.greaterThanOrEqual=" + DEFAULT_COPIES);

        // Get all the docList where copies is greater than or equal to UPDATED_COPIES
        defaultDocShouldNotBeFound("copies.greaterThanOrEqual=" + UPDATED_COPIES);
    }

    @Test
    @Transactional
    void getAllDocsByCopiesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where copies is less than or equal to DEFAULT_COPIES
        defaultDocShouldBeFound("copies.lessThanOrEqual=" + DEFAULT_COPIES);

        // Get all the docList where copies is less than or equal to SMALLER_COPIES
        defaultDocShouldNotBeFound("copies.lessThanOrEqual=" + SMALLER_COPIES);
    }

    @Test
    @Transactional
    void getAllDocsByCopiesIsLessThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where copies is less than DEFAULT_COPIES
        defaultDocShouldNotBeFound("copies.lessThan=" + DEFAULT_COPIES);

        // Get all the docList where copies is less than UPDATED_COPIES
        defaultDocShouldBeFound("copies.lessThan=" + UPDATED_COPIES);
    }

    @Test
    @Transactional
    void getAllDocsByCopiesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where copies is greater than DEFAULT_COPIES
        defaultDocShouldNotBeFound("copies.greaterThan=" + DEFAULT_COPIES);

        // Get all the docList where copies is greater than SMALLER_COPIES
        defaultDocShouldBeFound("copies.greaterThan=" + SMALLER_COPIES);
    }

    @Test
    @Transactional
    void getAllDocsByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where pageNumber equals to DEFAULT_PAGE_NUMBER
        defaultDocShouldBeFound("pageNumber.equals=" + DEFAULT_PAGE_NUMBER);

        // Get all the docList where pageNumber equals to UPDATED_PAGE_NUMBER
        defaultDocShouldNotBeFound("pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocsByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where pageNumber in DEFAULT_PAGE_NUMBER or UPDATED_PAGE_NUMBER
        defaultDocShouldBeFound("pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER);

        // Get all the docList where pageNumber equals to UPDATED_PAGE_NUMBER
        defaultDocShouldNotBeFound("pageNumber.in=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocsByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where pageNumber is not null
        defaultDocShouldBeFound("pageNumber.specified=true");

        // Get all the docList where pageNumber is null
        defaultDocShouldNotBeFound("pageNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where pageNumber is greater than or equal to DEFAULT_PAGE_NUMBER
        defaultDocShouldBeFound("pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER);

        // Get all the docList where pageNumber is greater than or equal to UPDATED_PAGE_NUMBER
        defaultDocShouldNotBeFound("pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocsByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where pageNumber is less than or equal to DEFAULT_PAGE_NUMBER
        defaultDocShouldBeFound("pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER);

        // Get all the docList where pageNumber is less than or equal to SMALLER_PAGE_NUMBER
        defaultDocShouldNotBeFound("pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocsByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where pageNumber is less than DEFAULT_PAGE_NUMBER
        defaultDocShouldNotBeFound("pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);

        // Get all the docList where pageNumber is less than UPDATED_PAGE_NUMBER
        defaultDocShouldBeFound("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocsByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where pageNumber is greater than DEFAULT_PAGE_NUMBER
        defaultDocShouldNotBeFound("pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);

        // Get all the docList where pageNumber is greater than SMALLER_PAGE_NUMBER
        defaultDocShouldBeFound("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocsByNumDocIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where numDoc equals to DEFAULT_NUM_DOC
        defaultDocShouldBeFound("numDoc.equals=" + DEFAULT_NUM_DOC);

        // Get all the docList where numDoc equals to UPDATED_NUM_DOC
        defaultDocShouldNotBeFound("numDoc.equals=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllDocsByNumDocIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where numDoc in DEFAULT_NUM_DOC or UPDATED_NUM_DOC
        defaultDocShouldBeFound("numDoc.in=" + DEFAULT_NUM_DOC + "," + UPDATED_NUM_DOC);

        // Get all the docList where numDoc equals to UPDATED_NUM_DOC
        defaultDocShouldNotBeFound("numDoc.in=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllDocsByNumDocIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where numDoc is not null
        defaultDocShouldBeFound("numDoc.specified=true");

        // Get all the docList where numDoc is null
        defaultDocShouldNotBeFound("numDoc.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByNumDocContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where numDoc contains DEFAULT_NUM_DOC
        defaultDocShouldBeFound("numDoc.contains=" + DEFAULT_NUM_DOC);

        // Get all the docList where numDoc contains UPDATED_NUM_DOC
        defaultDocShouldNotBeFound("numDoc.contains=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllDocsByNumDocNotContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where numDoc does not contain DEFAULT_NUM_DOC
        defaultDocShouldNotBeFound("numDoc.doesNotContain=" + DEFAULT_NUM_DOC);

        // Get all the docList where numDoc does not contain UPDATED_NUM_DOC
        defaultDocShouldBeFound("numDoc.doesNotContain=" + UPDATED_NUM_DOC);
    }

    @Test
    @Transactional
    void getAllDocsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where filename equals to DEFAULT_FILENAME
        defaultDocShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the docList where filename equals to UPDATED_FILENAME
        defaultDocShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllDocsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultDocShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the docList where filename equals to UPDATED_FILENAME
        defaultDocShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllDocsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where filename is not null
        defaultDocShouldBeFound("filename.specified=true");

        // Get all the docList where filename is null
        defaultDocShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    void getAllDocsByFilenameContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where filename contains DEFAULT_FILENAME
        defaultDocShouldBeFound("filename.contains=" + DEFAULT_FILENAME);

        // Get all the docList where filename contains UPDATED_FILENAME
        defaultDocShouldNotBeFound("filename.contains=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllDocsByFilenameNotContainsSomething() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList where filename does not contain DEFAULT_FILENAME
        defaultDocShouldNotBeFound("filename.doesNotContain=" + DEFAULT_FILENAME);

        // Get all the docList where filename does not contain UPDATED_FILENAME
        defaultDocShouldBeFound("filename.doesNotContain=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllDocsByPublisherIsEqualToSomething() throws Exception {
        DocPublisher publisher;
        if (TestUtil.findAll(em, DocPublisher.class).isEmpty()) {
            docRepository.saveAndFlush(doc);
            publisher = DocPublisherResourceIT.createEntity(em);
        } else {
            publisher = TestUtil.findAll(em, DocPublisher.class).get(0);
        }
        em.persist(publisher);
        em.flush();
        doc.setPublisher(publisher);
        docRepository.saveAndFlush(doc);
        Long publisherId = publisher.getId();

        // Get all the docList where publisher equals to publisherId
        defaultDocShouldBeFound("publisherId.equals=" + publisherId);

        // Get all the docList where publisher equals to (publisherId + 1)
        defaultDocShouldNotBeFound("publisherId.equals=" + (publisherId + 1));
    }

    @Test
    @Transactional
    void getAllDocsByCollectionIsEqualToSomething() throws Exception {
        DocCollection collection;
        if (TestUtil.findAll(em, DocCollection.class).isEmpty()) {
            docRepository.saveAndFlush(doc);
            collection = DocCollectionResourceIT.createEntity(em);
        } else {
            collection = TestUtil.findAll(em, DocCollection.class).get(0);
        }
        em.persist(collection);
        em.flush();
        doc.setCollection(collection);
        docRepository.saveAndFlush(doc);
        Long collectionId = collection.getId();

        // Get all the docList where collection equals to collectionId
        defaultDocShouldBeFound("collectionId.equals=" + collectionId);

        // Get all the docList where collection equals to (collectionId + 1)
        defaultDocShouldNotBeFound("collectionId.equals=" + (collectionId + 1));
    }

    @Test
    @Transactional
    void getAllDocsByFormatIsEqualToSomething() throws Exception {
        DocFormat format;
        if (TestUtil.findAll(em, DocFormat.class).isEmpty()) {
            docRepository.saveAndFlush(doc);
            format = DocFormatResourceIT.createEntity(em);
        } else {
            format = TestUtil.findAll(em, DocFormat.class).get(0);
        }
        em.persist(format);
        em.flush();
        doc.setFormat(format);
        docRepository.saveAndFlush(doc);
        Long formatId = format.getId();

        // Get all the docList where format equals to formatId
        defaultDocShouldBeFound("formatId.equals=" + formatId);

        // Get all the docList where format equals to (formatId + 1)
        defaultDocShouldNotBeFound("formatId.equals=" + (formatId + 1));
    }

    @Test
    @Transactional
    void getAllDocsByLangueIsEqualToSomething() throws Exception {
        Language langue;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            docRepository.saveAndFlush(doc);
            langue = LanguageResourceIT.createEntity(em);
        } else {
            langue = TestUtil.findAll(em, Language.class).get(0);
        }
        em.persist(langue);
        em.flush();
        doc.setLangue(langue);
        docRepository.saveAndFlush(doc);
        Long langueId = langue.getId();

        // Get all the docList where langue equals to langueId
        defaultDocShouldBeFound("langueId.equals=" + langueId);

        // Get all the docList where langue equals to (langueId + 1)
        defaultDocShouldNotBeFound("langueId.equals=" + (langueId + 1));
    }

    @Test
    @Transactional
    void getAllDocsByMaintopicIsEqualToSomething() throws Exception {
        DocTopic maintopic;
        if (TestUtil.findAll(em, DocTopic.class).isEmpty()) {
            docRepository.saveAndFlush(doc);
            maintopic = DocTopicResourceIT.createEntity(em);
        } else {
            maintopic = TestUtil.findAll(em, DocTopic.class).get(0);
        }
        em.persist(maintopic);
        em.flush();
        doc.setMaintopic(maintopic);
        docRepository.saveAndFlush(doc);
        Long maintopicId = maintopic.getId();

        // Get all the docList where maintopic equals to maintopicId
        defaultDocShouldBeFound("maintopicId.equals=" + maintopicId);

        // Get all the docList where maintopic equals to (maintopicId + 1)
        defaultDocShouldNotBeFound("maintopicId.equals=" + (maintopicId + 1));
    }

    @Test
    @Transactional
    void getAllDocsByMainAuthorIsEqualToSomething() throws Exception {
        DocAuthor mainAuthor;
        if (TestUtil.findAll(em, DocAuthor.class).isEmpty()) {
            docRepository.saveAndFlush(doc);
            mainAuthor = DocAuthorResourceIT.createEntity(em);
        } else {
            mainAuthor = TestUtil.findAll(em, DocAuthor.class).get(0);
        }
        em.persist(mainAuthor);
        em.flush();
        doc.setMainAuthor(mainAuthor);
        docRepository.saveAndFlush(doc);
        Long mainAuthorId = mainAuthor.getId();

        // Get all the docList where mainAuthor equals to mainAuthorId
        defaultDocShouldBeFound("mainAuthorId.equals=" + mainAuthorId);

        // Get all the docList where mainAuthor equals to (mainAuthorId + 1)
        defaultDocShouldNotBeFound("mainAuthorId.equals=" + (mainAuthorId + 1));
    }

    @Test
    @Transactional
    void getAllDocsByDocCategoryIsEqualToSomething() throws Exception {
        DocCategory docCategory;
        if (TestUtil.findAll(em, DocCategory.class).isEmpty()) {
            docRepository.saveAndFlush(doc);
            docCategory = DocCategoryResourceIT.createEntity(em);
        } else {
            docCategory = TestUtil.findAll(em, DocCategory.class).get(0);
        }
        em.persist(docCategory);
        em.flush();
        doc.setDocCategory(docCategory);
        docRepository.saveAndFlush(doc);
        Long docCategoryId = docCategory.getId();

        // Get all the docList where docCategory equals to docCategoryId
        defaultDocShouldBeFound("docCategoryId.equals=" + docCategoryId);

        // Get all the docList where docCategory equals to (docCategoryId + 1)
        defaultDocShouldNotBeFound("docCategoryId.equals=" + (docCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocShouldBeFound(String filter) throws Exception {
        restDocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doc.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].publishYear").value(hasItem(DEFAULT_PUBLISH_YEAR)))
            .andExpect(jsonPath("$.[*].coverImgPath").value(hasItem(DEFAULT_COVER_IMG_PATH)))
            .andExpect(jsonPath("$.[*].editionNumer").value(hasItem(DEFAULT_EDITION_NUMER)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].startReadingDate").value(hasItem(DEFAULT_START_READING_DATE.toString())))
            .andExpect(jsonPath("$.[*].endReadingDate").value(hasItem(DEFAULT_END_READING_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].copies").value(hasItem(DEFAULT_COPIES)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].numDoc").value(hasItem(DEFAULT_NUM_DOC)))
            .andExpect(jsonPath("$.[*].myNotes").value(hasItem(DEFAULT_MY_NOTES.toString())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].toc").value(hasItem(DEFAULT_TOC.toString())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)));

        // Check, that the count call also returns 1
        restDocMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocShouldNotBeFound(String filter) throws Exception {
        restDocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDoc() throws Exception {
        // Get the doc
        restDocMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoc() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        int databaseSizeBeforeUpdate = docRepository.findAll().size();

        // Update the doc
        Doc updatedDoc = docRepository.findById(doc.getId()).get();
        // Disconnect from session so that the updates on updatedDoc are not directly saved in db
        em.detach(updatedDoc);
        updatedDoc
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .publishYear(UPDATED_PUBLISH_YEAR)
            .coverImgPath(UPDATED_COVER_IMG_PATH)
            .editionNumer(UPDATED_EDITION_NUMER)
            .summary(UPDATED_SUMMARY)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .startReadingDate(UPDATED_START_READING_DATE)
            .endReadingDate(UPDATED_END_READING_DATE)
            .price(UPDATED_PRICE)
            .copies(UPDATED_COPIES)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .numDoc(UPDATED_NUM_DOC)
            .myNotes(UPDATED_MY_NOTES)
            .keywords(UPDATED_KEYWORDS)
            .toc(UPDATED_TOC)
            .filename(UPDATED_FILENAME);

        restDocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDoc.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDoc))
            )
            .andExpect(status().isOk());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
        Doc testDoc = docList.get(docList.size() - 1);
        assertThat(testDoc.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDoc.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testDoc.getPublishYear()).isEqualTo(UPDATED_PUBLISH_YEAR);
        assertThat(testDoc.getCoverImgPath()).isEqualTo(UPDATED_COVER_IMG_PATH);
        assertThat(testDoc.getEditionNumer()).isEqualTo(UPDATED_EDITION_NUMER);
        assertThat(testDoc.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testDoc.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testDoc.getStartReadingDate()).isEqualTo(UPDATED_START_READING_DATE);
        assertThat(testDoc.getEndReadingDate()).isEqualTo(UPDATED_END_READING_DATE);
        assertThat(testDoc.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDoc.getCopies()).isEqualTo(UPDATED_COPIES);
        assertThat(testDoc.getPageNumber()).isEqualTo(UPDATED_PAGE_NUMBER);
        assertThat(testDoc.getNumDoc()).isEqualTo(UPDATED_NUM_DOC);
        assertThat(testDoc.getMyNotes()).isEqualTo(UPDATED_MY_NOTES);
        assertThat(testDoc.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testDoc.getToc()).isEqualTo(UPDATED_TOC);
        assertThat(testDoc.getFilename()).isEqualTo(UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void putNonExistingDoc() throws Exception {
        int databaseSizeBeforeUpdate = docRepository.findAll().size();
        doc.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doc.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doc))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoc() throws Exception {
        int databaseSizeBeforeUpdate = docRepository.findAll().size();
        doc.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doc))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoc() throws Exception {
        int databaseSizeBeforeUpdate = docRepository.findAll().size();
        doc.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocWithPatch() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        int databaseSizeBeforeUpdate = docRepository.findAll().size();

        // Update the doc using partial update
        Doc partialUpdatedDoc = new Doc();
        partialUpdatedDoc.setId(doc.getId());

        partialUpdatedDoc
            .publishYear(UPDATED_PUBLISH_YEAR)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .endReadingDate(UPDATED_END_READING_DATE)
            .price(UPDATED_PRICE)
            .filename(UPDATED_FILENAME);

        restDocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoc))
            )
            .andExpect(status().isOk());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
        Doc testDoc = docList.get(docList.size() - 1);
        assertThat(testDoc.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDoc.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testDoc.getPublishYear()).isEqualTo(UPDATED_PUBLISH_YEAR);
        assertThat(testDoc.getCoverImgPath()).isEqualTo(DEFAULT_COVER_IMG_PATH);
        assertThat(testDoc.getEditionNumer()).isEqualTo(DEFAULT_EDITION_NUMER);
        assertThat(testDoc.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testDoc.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testDoc.getStartReadingDate()).isEqualTo(DEFAULT_START_READING_DATE);
        assertThat(testDoc.getEndReadingDate()).isEqualTo(UPDATED_END_READING_DATE);
        assertThat(testDoc.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDoc.getCopies()).isEqualTo(DEFAULT_COPIES);
        assertThat(testDoc.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(testDoc.getNumDoc()).isEqualTo(DEFAULT_NUM_DOC);
        assertThat(testDoc.getMyNotes()).isEqualTo(DEFAULT_MY_NOTES);
        assertThat(testDoc.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testDoc.getToc()).isEqualTo(DEFAULT_TOC);
        assertThat(testDoc.getFilename()).isEqualTo(UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void fullUpdateDocWithPatch() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        int databaseSizeBeforeUpdate = docRepository.findAll().size();

        // Update the doc using partial update
        Doc partialUpdatedDoc = new Doc();
        partialUpdatedDoc.setId(doc.getId());

        partialUpdatedDoc
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .publishYear(UPDATED_PUBLISH_YEAR)
            .coverImgPath(UPDATED_COVER_IMG_PATH)
            .editionNumer(UPDATED_EDITION_NUMER)
            .summary(UPDATED_SUMMARY)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .startReadingDate(UPDATED_START_READING_DATE)
            .endReadingDate(UPDATED_END_READING_DATE)
            .price(UPDATED_PRICE)
            .copies(UPDATED_COPIES)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .numDoc(UPDATED_NUM_DOC)
            .myNotes(UPDATED_MY_NOTES)
            .keywords(UPDATED_KEYWORDS)
            .toc(UPDATED_TOC)
            .filename(UPDATED_FILENAME);

        restDocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoc))
            )
            .andExpect(status().isOk());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
        Doc testDoc = docList.get(docList.size() - 1);
        assertThat(testDoc.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDoc.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testDoc.getPublishYear()).isEqualTo(UPDATED_PUBLISH_YEAR);
        assertThat(testDoc.getCoverImgPath()).isEqualTo(UPDATED_COVER_IMG_PATH);
        assertThat(testDoc.getEditionNumer()).isEqualTo(UPDATED_EDITION_NUMER);
        assertThat(testDoc.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testDoc.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testDoc.getStartReadingDate()).isEqualTo(UPDATED_START_READING_DATE);
        assertThat(testDoc.getEndReadingDate()).isEqualTo(UPDATED_END_READING_DATE);
        assertThat(testDoc.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDoc.getCopies()).isEqualTo(UPDATED_COPIES);
        assertThat(testDoc.getPageNumber()).isEqualTo(UPDATED_PAGE_NUMBER);
        assertThat(testDoc.getNumDoc()).isEqualTo(UPDATED_NUM_DOC);
        assertThat(testDoc.getMyNotes()).isEqualTo(UPDATED_MY_NOTES);
        assertThat(testDoc.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testDoc.getToc()).isEqualTo(UPDATED_TOC);
        assertThat(testDoc.getFilename()).isEqualTo(UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void patchNonExistingDoc() throws Exception {
        int databaseSizeBeforeUpdate = docRepository.findAll().size();
        doc.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doc.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doc))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoc() throws Exception {
        int databaseSizeBeforeUpdate = docRepository.findAll().size();
        doc.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doc))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoc() throws Exception {
        int databaseSizeBeforeUpdate = docRepository.findAll().size();
        doc.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(doc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoc() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        int databaseSizeBeforeDelete = docRepository.findAll().size();

        // Delete the doc
        restDocMockMvc.perform(delete(ENTITY_API_URL_ID, doc.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
