package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.DocAuthor;
import com.perso.mydocs.repository.DocAuthorRepository;
import com.perso.mydocs.service.DocAuthorQueryService;
import com.perso.mydocs.service.DocAuthorService;
import com.perso.mydocs.service.criteria.DocAuthorCriteria;
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
 * REST controller for managing {@link com.perso.mydocs.domain.DocAuthor}.
 */
@RestController
@RequestMapping("/api")
public class DocAuthorResource {

    private final Logger log = LoggerFactory.getLogger(DocAuthorResource.class);

    private static final String ENTITY_NAME = "docAuthor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocAuthorService docAuthorService;

    private final DocAuthorRepository docAuthorRepository;

    private final DocAuthorQueryService docAuthorQueryService;

    public DocAuthorResource(
        DocAuthorService docAuthorService,
        DocAuthorRepository docAuthorRepository,
        DocAuthorQueryService docAuthorQueryService
    ) {
        this.docAuthorService = docAuthorService;
        this.docAuthorRepository = docAuthorRepository;
        this.docAuthorQueryService = docAuthorQueryService;
    }

    /**
     * {@code POST  /doc-authors} : Create a new docAuthor.
     *
     * @param docAuthor the docAuthor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docAuthor, or with status {@code 400 (Bad Request)} if the docAuthor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-authors")
    public ResponseEntity<DocAuthor> createDocAuthor(@RequestBody DocAuthor docAuthor) throws URISyntaxException {
        log.debug("REST request to save DocAuthor : {}", docAuthor);
        if (docAuthor.getId() != null) {
            throw new BadRequestAlertException("A new docAuthor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocAuthor result = docAuthorService.save(docAuthor);
        return ResponseEntity
            .created(new URI("/api/doc-authors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-authors/:id} : Updates an existing docAuthor.
     *
     * @param id the id of the docAuthor to save.
     * @param docAuthor the docAuthor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docAuthor,
     * or with status {@code 400 (Bad Request)} if the docAuthor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docAuthor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-authors/{id}")
    public ResponseEntity<DocAuthor> updateDocAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocAuthor docAuthor
    ) throws URISyntaxException {
        log.debug("REST request to update DocAuthor : {}, {}", id, docAuthor);
        if (docAuthor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docAuthor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docAuthorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocAuthor result = docAuthorService.update(docAuthor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docAuthor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-authors/:id} : Partial updates given fields of an existing docAuthor, field will ignore if it is null
     *
     * @param id the id of the docAuthor to save.
     * @param docAuthor the docAuthor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docAuthor,
     * or with status {@code 400 (Bad Request)} if the docAuthor is not valid,
     * or with status {@code 404 (Not Found)} if the docAuthor is not found,
     * or with status {@code 500 (Internal Server Error)} if the docAuthor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-authors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocAuthor> partialUpdateDocAuthor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocAuthor docAuthor
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocAuthor partially : {}, {}", id, docAuthor);
        if (docAuthor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docAuthor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docAuthorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocAuthor> result = docAuthorService.partialUpdate(docAuthor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docAuthor.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-authors} : get all the docAuthors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docAuthors in body.
	 
	[  c.p.mydocs.web.rest.DocAuthorResource    : Enter: getAllDocAuthors() with argument[s] = [DocAuthorCriteria{}, Page request [number: 0, size 20, sort: UNSORTED]]
	 
     */
    @GetMapping("/doc-authors")
    public ResponseEntity<List<DocAuthor>> getAllDocAuthors(
        DocAuthorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocAuthors by criteria: {}", criteria);

        Page<DocAuthor> page = docAuthorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-authors/count} : count all the docAuthors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-authors/count")
    public ResponseEntity<Long> countDocAuthors(DocAuthorCriteria criteria) {
        log.debug("REST request to count DocAuthors by criteria: {}", criteria);
        return ResponseEntity.ok().body(docAuthorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-authors/:id} : get the "id" docAuthor.
     *
     * @param id the id of the docAuthor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docAuthor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-authors/{id}")
    public ResponseEntity<DocAuthor> getDocAuthor(@PathVariable Long id) {
        log.debug("REST request to get DocAuthor : {}", id);
        Optional<DocAuthor> docAuthor = docAuthorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docAuthor);
    }

    /**
     * {@code DELETE  /doc-authors/:id} : delete the "id" docAuthor.
     *
     * @param id the id of the docAuthor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-authors/{id}")
    public ResponseEntity<Void> deleteDocAuthor(@PathVariable Long id) {
        log.debug("REST request to delete DocAuthor : {}", id);
        docAuthorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
