package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.DocCollection;
import com.perso.mydocs.repository.DocCollectionRepository;
import com.perso.mydocs.service.DocCollectionQueryService;
import com.perso.mydocs.service.DocCollectionService;
import com.perso.mydocs.service.criteria.DocCollectionCriteria;
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
 * REST controller for managing {@link com.perso.mydocs.domain.DocCollection}.
 */
@RestController
@RequestMapping("/api")
public class DocCollectionResource {

    private final Logger log = LoggerFactory.getLogger(DocCollectionResource.class);

    private static final String ENTITY_NAME = "docCollection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocCollectionService docCollectionService;

    private final DocCollectionRepository docCollectionRepository;

    private final DocCollectionQueryService docCollectionQueryService;

    public DocCollectionResource(
        DocCollectionService docCollectionService,
        DocCollectionRepository docCollectionRepository,
        DocCollectionQueryService docCollectionQueryService
    ) {
        this.docCollectionService = docCollectionService;
        this.docCollectionRepository = docCollectionRepository;
        this.docCollectionQueryService = docCollectionQueryService;
    }

    /**
     * {@code POST  /doc-collections} : Create a new docCollection.
     *
     * @param docCollection the docCollection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docCollection, or with status {@code 400 (Bad Request)} if the docCollection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-collections")
    public ResponseEntity<DocCollection> createDocCollection(@RequestBody DocCollection docCollection) throws URISyntaxException {
        log.debug("REST request to save DocCollection : {}", docCollection);
        if (docCollection.getId() != null) {
            throw new BadRequestAlertException("A new docCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocCollection result = docCollectionService.save(docCollection);
        return ResponseEntity
            .created(new URI("/api/doc-collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-collections/:id} : Updates an existing docCollection.
     *
     * @param id the id of the docCollection to save.
     * @param docCollection the docCollection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docCollection,
     * or with status {@code 400 (Bad Request)} if the docCollection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docCollection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-collections/{id}")
    public ResponseEntity<DocCollection> updateDocCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocCollection docCollection
    ) throws URISyntaxException {
        log.debug("REST request to update DocCollection : {}, {}", id, docCollection);
        if (docCollection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docCollection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docCollectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocCollection result = docCollectionService.update(docCollection);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docCollection.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-collections/:id} : Partial updates given fields of an existing docCollection, field will ignore if it is null
     *
     * @param id the id of the docCollection to save.
     * @param docCollection the docCollection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docCollection,
     * or with status {@code 400 (Bad Request)} if the docCollection is not valid,
     * or with status {@code 404 (Not Found)} if the docCollection is not found,
     * or with status {@code 500 (Internal Server Error)} if the docCollection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-collections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocCollection> partialUpdateDocCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocCollection docCollection
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocCollection partially : {}, {}", id, docCollection);
        if (docCollection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docCollection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docCollectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocCollection> result = docCollectionService.partialUpdate(docCollection);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docCollection.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-collections} : get all the docCollections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docCollections in body.
     */
    @GetMapping("/doc-collections")
    public ResponseEntity<List<DocCollection>> getAllDocCollections(
        DocCollectionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocCollections by criteria: {}", criteria);
        Page<DocCollection> page = docCollectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        System.out.println(" ******************   DocCollectionResource.getAllDocCollections()    *****************");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-collections/count} : count all the docCollections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-collections/count")
    public ResponseEntity<Long> countDocCollections(DocCollectionCriteria criteria) {
        log.debug("REST request to count DocCollections by criteria: {}", criteria);
        return ResponseEntity.ok().body(docCollectionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-collections/:id} : get the "id" docCollection.
     *
     * @param id the id of the docCollection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docCollection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-collections/{id}")
    public ResponseEntity<DocCollection> getDocCollection(@PathVariable Long id) {
        log.debug("REST request to get DocCollection : {}", id);
        Optional<DocCollection> docCollection = docCollectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docCollection);
    }

    /**
     * {@code DELETE  /doc-collections/:id} : delete the "id" docCollection.
     *
     * @param id the id of the docCollection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-collections/{id}")
    public ResponseEntity<Void> deleteDocCollection(@PathVariable Long id) {
        log.debug("REST request to delete DocCollection : {}", id);
        docCollectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
