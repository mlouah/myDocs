package com.perso.mydocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.perso.mydocs.IntegrationTest;
import com.perso.mydocs.domain.DocTopic;
import com.perso.mydocs.domain.Domaine;
import com.perso.mydocs.repository.DomaineRepository;
import com.perso.mydocs.service.criteria.DomaineCriteria;
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
 * Integration tests for the {@link DomaineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomaineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/domaines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomaineMockMvc;

    private Domaine domaine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domaine createEntity(EntityManager em) {
        Domaine domaine = new Domaine().name(DEFAULT_NAME).code(DEFAULT_CODE).notes(DEFAULT_NOTES);
        return domaine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domaine createUpdatedEntity(EntityManager em) {
        Domaine domaine = new Domaine().name(UPDATED_NAME).code(UPDATED_CODE).notes(UPDATED_NOTES);
        return domaine;
    }

    @BeforeEach
    public void initTest() {
        domaine = createEntity(em);
    }

    @Test
    @Transactional
    void createDomaine() throws Exception {
        int databaseSizeBeforeCreate = domaineRepository.findAll().size();
        // Create the Domaine
        restDomaineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaine)))
            .andExpect(status().isCreated());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeCreate + 1);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDomaine.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDomaine.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createDomaineWithExistingId() throws Exception {
        // Create the Domaine with an existing ID
        domaine.setId(1L);

        int databaseSizeBeforeCreate = domaineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomaineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaine)))
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDomaines() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    void getDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get the domaine
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL_ID, domaine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domaine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    void getDomainesByIdFiltering() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        Long id = domaine.getId();

        defaultDomaineShouldBeFound("id.equals=" + id);
        defaultDomaineShouldNotBeFound("id.notEquals=" + id);

        defaultDomaineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDomaineShouldNotBeFound("id.greaterThan=" + id);

        defaultDomaineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDomaineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDomainesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where name equals to DEFAULT_NAME
        defaultDomaineShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the domaineList where name equals to UPDATED_NAME
        defaultDomaineShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDomainesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDomaineShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the domaineList where name equals to UPDATED_NAME
        defaultDomaineShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDomainesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where name is not null
        defaultDomaineShouldBeFound("name.specified=true");

        // Get all the domaineList where name is null
        defaultDomaineShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDomainesByNameContainsSomething() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where name contains DEFAULT_NAME
        defaultDomaineShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the domaineList where name contains UPDATED_NAME
        defaultDomaineShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDomainesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where name does not contain DEFAULT_NAME
        defaultDomaineShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the domaineList where name does not contain UPDATED_NAME
        defaultDomaineShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDomainesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where code equals to DEFAULT_CODE
        defaultDomaineShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the domaineList where code equals to UPDATED_CODE
        defaultDomaineShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDomainesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDomaineShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the domaineList where code equals to UPDATED_CODE
        defaultDomaineShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDomainesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where code is not null
        defaultDomaineShouldBeFound("code.specified=true");

        // Get all the domaineList where code is null
        defaultDomaineShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDomainesByCodeContainsSomething() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where code contains DEFAULT_CODE
        defaultDomaineShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the domaineList where code contains UPDATED_CODE
        defaultDomaineShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDomainesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList where code does not contain DEFAULT_CODE
        defaultDomaineShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the domaineList where code does not contain UPDATED_CODE
        defaultDomaineShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDomainesByTopicIsEqualToSomething() throws Exception {
        DocTopic topic;
        if (TestUtil.findAll(em, DocTopic.class).isEmpty()) {
            domaineRepository.saveAndFlush(domaine);
            topic = DocTopicResourceIT.createEntity(em);
        } else {
            topic = TestUtil.findAll(em, DocTopic.class).get(0);
        }
        em.persist(topic);
        em.flush();
        domaine.addTopic(topic);
        domaineRepository.saveAndFlush(domaine);
        Long topicId = topic.getId();

        // Get all the domaineList where topic equals to topicId
        defaultDomaineShouldBeFound("topicId.equals=" + topicId);

        // Get all the domaineList where topic equals to (topicId + 1)
        defaultDomaineShouldNotBeFound("topicId.equals=" + (topicId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDomaineShouldBeFound(String filter) throws Exception {
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));

        // Check, that the count call also returns 1
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDomaineShouldNotBeFound(String filter) throws Exception {
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDomaineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDomaine() throws Exception {
        // Get the domaine
        restDomaineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Update the domaine
        Domaine updatedDomaine = domaineRepository.findById(domaine.getId()).get();
        // Disconnect from session so that the updates on updatedDomaine are not directly saved in db
        em.detach(updatedDomaine);
        updatedDomaine.name(UPDATED_NAME).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDomaine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDomaine))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDomaine.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDomaine.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domaine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomaineWithPatch() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Update the domaine using partial update
        Domaine partialUpdatedDomaine = new Domaine();
        partialUpdatedDomaine.setId(domaine.getId());

        partialUpdatedDomaine.notes(UPDATED_NOTES);

        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaine))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDomaine.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDomaine.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateDomaineWithPatch() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Update the domaine using partial update
        Domaine partialUpdatedDomaine = new Domaine();
        partialUpdatedDomaine.setId(domaine.getId());

        partialUpdatedDomaine.name(UPDATED_NAME).code(UPDATED_CODE).notes(UPDATED_NOTES);

        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaine))
            )
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDomaine.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDomaine.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domaine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();
        domaine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(domaine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeDelete = domaineRepository.findAll().size();

        // Delete the domaine
        restDomaineMockMvc
            .perform(delete(ENTITY_API_URL_ID, domaine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
