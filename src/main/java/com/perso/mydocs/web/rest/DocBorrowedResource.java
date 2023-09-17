package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.DocBorrowed;
import com.perso.mydocs.repository.DocBorrowedRepository;
import com.perso.mydocs.service.DocBorrowedQueryService;
import com.perso.mydocs.service.DocBorrowedService;
import com.perso.mydocs.service.criteria.DocBorrowedCriteria;
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
 * REST controller for managing {@link com.perso.mydocs.domain.DocBorrowed}.
 */
@RestController
@RequestMapping("/api")
public class DocBorrowedResource {

    private final Logger log = LoggerFactory.getLogger(DocBorrowedResource.class);

    private static final String ENTITY_NAME = "docBorrowed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocBorrowedService docBorrowedService;

    private final DocBorrowedRepository docBorrowedRepository;

    private final DocBorrowedQueryService docBorrowedQueryService;

    public DocBorrowedResource(
        DocBorrowedService docBorrowedService,
        DocBorrowedRepository docBorrowedRepository,
        DocBorrowedQueryService docBorrowedQueryService
    ) {
        this.docBorrowedService = docBorrowedService;
        this.docBorrowedRepository = docBorrowedRepository;
        this.docBorrowedQueryService = docBorrowedQueryService;
    }

    /**
     * {@code POST  /doc-borroweds} : Create a new docBorrowed.
     *
     * @param docBorrowed the docBorrowed to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docBorrowed, or with status {@code 400 (Bad Request)} if the docBorrowed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-borroweds")
    public ResponseEntity<DocBorrowed> createDocBorrowed(@RequestBody DocBorrowed docBorrowed) throws URISyntaxException {
        log.debug("REST request to save DocBorrowed : {}", docBorrowed);
        if (docBorrowed.getId() != null) {
            throw new BadRequestAlertException("A new docBorrowed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocBorrowed result = docBorrowedService.save(docBorrowed);
        return ResponseEntity
            .created(new URI("/api/doc-borroweds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-borroweds/:id} : Updates an existing docBorrowed.
     *
     * @param id the id of the docBorrowed to save.
     * @param docBorrowed the docBorrowed to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docBorrowed,
     * or with status {@code 400 (Bad Request)} if the docBorrowed is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docBorrowed couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-borroweds/{id}")
    public ResponseEntity<DocBorrowed> updateDocBorrowed(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocBorrowed docBorrowed
    ) throws URISyntaxException {
        log.debug("REST request to update DocBorrowed : {}, {}", id, docBorrowed);
        if (docBorrowed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docBorrowed.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docBorrowedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocBorrowed result = docBorrowedService.update(docBorrowed);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docBorrowed.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-borroweds/:id} : Partial updates given fields of an existing docBorrowed, field will ignore if it is null
     *
     * @param id the id of the docBorrowed to save.
     * @param docBorrowed the docBorrowed to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docBorrowed,
     * or with status {@code 400 (Bad Request)} if the docBorrowed is not valid,
     * or with status {@code 404 (Not Found)} if the docBorrowed is not found,
     * or with status {@code 500 (Internal Server Error)} if the docBorrowed couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-borroweds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocBorrowed> partialUpdateDocBorrowed(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocBorrowed docBorrowed
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocBorrowed partially : {}, {}", id, docBorrowed);
        if (docBorrowed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docBorrowed.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docBorrowedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocBorrowed> result = docBorrowedService.partialUpdate(docBorrowed);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docBorrowed.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-borroweds} : get all the docBorroweds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docBorroweds in body.
     */
    @GetMapping("/doc-borroweds")
    public ResponseEntity<List<DocBorrowed>> getAllDocBorroweds(
        DocBorrowedCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocBorroweds by criteria: {}", criteria);
        Page<DocBorrowed> page = docBorrowedQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-borroweds/count} : count all the docBorroweds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-borroweds/count")
    public ResponseEntity<Long> countDocBorroweds(DocBorrowedCriteria criteria) {
        log.debug("REST request to count DocBorroweds by criteria: {}", criteria);
        return ResponseEntity.ok().body(docBorrowedQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-borroweds/:id} : get the "id" docBorrowed.
     *
     * @param id the id of the docBorrowed to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docBorrowed, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-borroweds/{id}")
    public ResponseEntity<DocBorrowed> getDocBorrowed(@PathVariable Long id) {
        log.debug("REST request to get DocBorrowed : {}", id);
        Optional<DocBorrowed> docBorrowed = docBorrowedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docBorrowed);
    }

    /**
     * {@code DELETE  /doc-borroweds/:id} : delete the "id" docBorrowed.
     *
     * @param id the id of the docBorrowed to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-borroweds/{id}")
    public ResponseEntity<Void> deleteDocBorrowed(@PathVariable Long id) {
        log.debug("REST request to delete DocBorrowed : {}", id);
        docBorrowedService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
