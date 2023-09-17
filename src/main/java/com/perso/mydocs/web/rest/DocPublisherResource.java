package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.DocPublisher;
import com.perso.mydocs.repository.DocPublisherRepository;
import com.perso.mydocs.service.DocPublisherQueryService;
import com.perso.mydocs.service.DocPublisherService;
import com.perso.mydocs.service.criteria.DocPublisherCriteria;
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
 * REST controller for managing {@link com.perso.mydocs.domain.DocPublisher}.
 */
@RestController
@RequestMapping("/api")
public class DocPublisherResource {

    private final Logger log = LoggerFactory.getLogger(DocPublisherResource.class);

    private static final String ENTITY_NAME = "docPublisher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocPublisherService docPublisherService;

    private final DocPublisherRepository docPublisherRepository;

    private final DocPublisherQueryService docPublisherQueryService;

    public DocPublisherResource(
        DocPublisherService docPublisherService,
        DocPublisherRepository docPublisherRepository,
        DocPublisherQueryService docPublisherQueryService
    ) {
        this.docPublisherService = docPublisherService;
        this.docPublisherRepository = docPublisherRepository;
        this.docPublisherQueryService = docPublisherQueryService;
    }

    /**
     * {@code POST  /doc-publishers} : Create a new docPublisher.
     *
     * @param docPublisher the docPublisher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docPublisher, or with status {@code 400 (Bad Request)} if the docPublisher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-publishers")
    public ResponseEntity<DocPublisher> createDocPublisher(@RequestBody DocPublisher docPublisher) throws URISyntaxException {
        log.debug("REST request to save DocPublisher : {}", docPublisher);
        if (docPublisher.getId() != null) {
            throw new BadRequestAlertException("A new docPublisher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocPublisher result = docPublisherService.save(docPublisher);
        return ResponseEntity
            .created(new URI("/api/doc-publishers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-publishers/:id} : Updates an existing docPublisher.
     *
     * @param id the id of the docPublisher to save.
     * @param docPublisher the docPublisher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docPublisher,
     * or with status {@code 400 (Bad Request)} if the docPublisher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docPublisher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-publishers/{id}")
    public ResponseEntity<DocPublisher> updateDocPublisher(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocPublisher docPublisher
    ) throws URISyntaxException {
        log.debug("REST request to update DocPublisher : {}, {}", id, docPublisher);
        if (docPublisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docPublisher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docPublisherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocPublisher result = docPublisherService.update(docPublisher);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docPublisher.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-publishers/:id} : Partial updates given fields of an existing docPublisher, field will ignore if it is null
     *
     * @param id the id of the docPublisher to save.
     * @param docPublisher the docPublisher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docPublisher,
     * or with status {@code 400 (Bad Request)} if the docPublisher is not valid,
     * or with status {@code 404 (Not Found)} if the docPublisher is not found,
     * or with status {@code 500 (Internal Server Error)} if the docPublisher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-publishers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocPublisher> partialUpdateDocPublisher(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocPublisher docPublisher
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocPublisher partially : {}, {}", id, docPublisher);
        if (docPublisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docPublisher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docPublisherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocPublisher> result = docPublisherService.partialUpdate(docPublisher);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docPublisher.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-publishers} : get all the docPublishers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docPublishers in body.
     */
    @GetMapping("/doc-publishers")
    public ResponseEntity<List<DocPublisher>> getAllDocPublishers(
        DocPublisherCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocPublishers by criteria: {}", criteria);
        Page<DocPublisher> page = docPublisherQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-publishers/count} : count all the docPublishers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-publishers/count")
    public ResponseEntity<Long> countDocPublishers(DocPublisherCriteria criteria) {
        log.debug("REST request to count DocPublishers by criteria: {}", criteria);
        return ResponseEntity.ok().body(docPublisherQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-publishers/:id} : get the "id" docPublisher.
     *
     * @param id the id of the docPublisher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docPublisher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-publishers/{id}")
    public ResponseEntity<DocPublisher> getDocPublisher(@PathVariable Long id) {
        log.debug("REST request to get DocPublisher : {}", id);
        Optional<DocPublisher> docPublisher = docPublisherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docPublisher);
    }

    /**
     * {@code DELETE  /doc-publishers/:id} : delete the "id" docPublisher.
     *
     * @param id the id of the docPublisher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-publishers/{id}")
    public ResponseEntity<Void> deleteDocPublisher(@PathVariable Long id) {
        log.debug("REST request to delete DocPublisher : {}", id);
        docPublisherService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
