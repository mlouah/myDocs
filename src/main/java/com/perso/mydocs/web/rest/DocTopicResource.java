package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.DocTopic;
import com.perso.mydocs.repository.DocTopicRepository;
import com.perso.mydocs.service.DocTopicQueryService;
import com.perso.mydocs.service.DocTopicService;
import com.perso.mydocs.service.criteria.DocTopicCriteria;
import com.perso.mydocs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.perso.mydocs.domain.DocTopic}.
 */
@RestController
@RequestMapping("/api")
public class DocTopicResource {

    private final Logger log = LoggerFactory.getLogger(DocTopicResource.class);

    private static final String ENTITY_NAME = "docTopic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocTopicService docTopicService;

    private final DocTopicRepository docTopicRepository;

    private final DocTopicQueryService docTopicQueryService;

    public DocTopicResource(
        DocTopicService docTopicService,
        DocTopicRepository docTopicRepository,
        DocTopicQueryService docTopicQueryService
    ) {
        this.docTopicService = docTopicService;
        this.docTopicRepository = docTopicRepository;
        this.docTopicQueryService = docTopicQueryService;
    }

    /**
     * {@code POST  /doc-topics} : Create a new docTopic.
     *
     * @param docTopic the docTopic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docTopic, or with status {@code 400 (Bad Request)} if the docTopic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-topics")
    public ResponseEntity<DocTopic> createDocTopic(@RequestBody DocTopic docTopic) throws URISyntaxException {
        log.debug("REST request to save DocTopic : {}", docTopic);
        if (docTopic.getId() != null) {
            throw new BadRequestAlertException("A new docTopic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocTopic result = docTopicService.save(docTopic);
        return ResponseEntity
            .created(new URI("/api/doc-topics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-topics/:id} : Updates an existing docTopic.
     *
     * @param id the id of the docTopic to save.
     * @param docTopic the docTopic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docTopic,
     * or with status {@code 400 (Bad Request)} if the docTopic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docTopic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-topics/{id}")
    public ResponseEntity<DocTopic> updateDocTopic(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocTopic docTopic
    ) throws URISyntaxException {
        log.debug("REST request to update DocTopic : {}, {}", id, docTopic);
        if (docTopic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docTopic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docTopicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocTopic result = docTopicService.update(docTopic);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docTopic.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-topics/:id} : Partial updates given fields of an existing docTopic, field will ignore if it is null
     *
     * @param id the id of the docTopic to save.
     * @param docTopic the docTopic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docTopic,
     * or with status {@code 400 (Bad Request)} if the docTopic is not valid,
     * or with status {@code 404 (Not Found)} if the docTopic is not found,
     * or with status {@code 500 (Internal Server Error)} if the docTopic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-topics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocTopic> partialUpdateDocTopic(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocTopic docTopic
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocTopic partially : {}, {}", id, docTopic);
        if (docTopic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docTopic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docTopicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocTopic> result = docTopicService.partialUpdate(docTopic);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docTopic.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-topics} : get all the docTopics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docTopics in body.
     */
    @GetMapping("/doc-topics")
    public ResponseEntity<List<DocTopic>> getAllDocTopics(
        DocTopicCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocTopics by criteria: {}", criteria);
        Page<DocTopic> page = docTopicQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-topics/count} : count all the docTopics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-topics/count")
    public ResponseEntity<Long> countDocTopics(DocTopicCriteria criteria) {
        log.debug("REST request to count DocTopics by criteria: {}", criteria);
        return ResponseEntity.ok().body(docTopicQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-topics/:id} : get the "id" docTopic.
     *
     * @param id the id of the docTopic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docTopic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-topics/{id}")
    public ResponseEntity<DocTopic> getDocTopic(@PathVariable Long id) {
        log.debug("REST request to get DocTopic : {}", id);
        Optional<DocTopic> docTopic = docTopicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docTopic);
    }

    /**
     * {@code DELETE  /doc-topics/:id} : delete the "id" docTopic.
     *
     * @param id the id of the docTopic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-topics/{id}")
    public ResponseEntity<Void> deleteDocTopic(@PathVariable Long id) {
        log.debug("REST request to delete DocTopic : {}", id);
        docTopicService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
