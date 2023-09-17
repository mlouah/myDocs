package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.DocFormat;
import com.perso.mydocs.repository.DocFormatRepository;
import com.perso.mydocs.service.DocFormatQueryService;
import com.perso.mydocs.service.DocFormatService;
import com.perso.mydocs.service.criteria.DocFormatCriteria;
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
 * REST controller for managing {@link com.perso.mydocs.domain.DocFormat}.
 */
@RestController
@RequestMapping("/api")
public class DocFormatResource {

    private final Logger log = LoggerFactory.getLogger(DocFormatResource.class);

    private static final String ENTITY_NAME = "docFormat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocFormatService docFormatService;

    private final DocFormatRepository docFormatRepository;

    private final DocFormatQueryService docFormatQueryService;

    public DocFormatResource(
        DocFormatService docFormatService,
        DocFormatRepository docFormatRepository,
        DocFormatQueryService docFormatQueryService
    ) {
        this.docFormatService = docFormatService;
        this.docFormatRepository = docFormatRepository;
        this.docFormatQueryService = docFormatQueryService;
    }

    /**
     * {@code POST  /doc-formats} : Create a new docFormat.
     *
     * @param docFormat the docFormat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docFormat, or with status {@code 400 (Bad Request)} if the docFormat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-formats")
    public ResponseEntity<DocFormat> createDocFormat(@RequestBody DocFormat docFormat) throws URISyntaxException {
        log.debug("REST request to save DocFormat : {}", docFormat);
        if (docFormat.getId() != null) {
            throw new BadRequestAlertException("A new docFormat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocFormat result = docFormatService.save(docFormat);
        return ResponseEntity
            .created(new URI("/api/doc-formats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-formats/:id} : Updates an existing docFormat.
     *
     * @param id the id of the docFormat to save.
     * @param docFormat the docFormat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docFormat,
     * or with status {@code 400 (Bad Request)} if the docFormat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docFormat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-formats/{id}")
    public ResponseEntity<DocFormat> updateDocFormat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocFormat docFormat
    ) throws URISyntaxException {
        log.debug("REST request to update DocFormat : {}, {}", id, docFormat);
        if (docFormat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docFormat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docFormatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocFormat result = docFormatService.update(docFormat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docFormat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-formats/:id} : Partial updates given fields of an existing docFormat, field will ignore if it is null
     *
     * @param id the id of the docFormat to save.
     * @param docFormat the docFormat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docFormat,
     * or with status {@code 400 (Bad Request)} if the docFormat is not valid,
     * or with status {@code 404 (Not Found)} if the docFormat is not found,
     * or with status {@code 500 (Internal Server Error)} if the docFormat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-formats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocFormat> partialUpdateDocFormat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocFormat docFormat
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocFormat partially : {}, {}", id, docFormat);
        if (docFormat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docFormat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docFormatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocFormat> result = docFormatService.partialUpdate(docFormat);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docFormat.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-formats} : get all the docFormats.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docFormats in body.
     */
    @GetMapping("/doc-formats")
    public ResponseEntity<List<DocFormat>> getAllDocFormats(
        DocFormatCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocFormats by criteria: {}", criteria);
        Page<DocFormat> page = docFormatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-formats/count} : count all the docFormats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-formats/count")
    public ResponseEntity<Long> countDocFormats(DocFormatCriteria criteria) {
        log.debug("REST request to count DocFormats by criteria: {}", criteria);
        return ResponseEntity.ok().body(docFormatQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-formats/:id} : get the "id" docFormat.
     *
     * @param id the id of the docFormat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docFormat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-formats/{id}")
    public ResponseEntity<DocFormat> getDocFormat(@PathVariable Long id) {
        log.debug("REST request to get DocFormat : {}", id);
        Optional<DocFormat> docFormat = docFormatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docFormat);
    }

    /**
     * {@code DELETE  /doc-formats/:id} : delete the "id" docFormat.
     *
     * @param id the id of the docFormat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-formats/{id}")
    public ResponseEntity<Void> deleteDocFormat(@PathVariable Long id) {
        log.debug("REST request to delete DocFormat : {}", id);
        docFormatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
