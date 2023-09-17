package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.DocCategory;
import com.perso.mydocs.repository.DocCategoryRepository;
import com.perso.mydocs.service.DocCategoryQueryService;
import com.perso.mydocs.service.DocCategoryService;
import com.perso.mydocs.service.criteria.DocCategoryCriteria;
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
 * REST controller for managing {@link com.perso.mydocs.domain.DocCategory}.
 */
@RestController
@RequestMapping("/api")
public class DocCategoryResource {

    private final Logger log = LoggerFactory.getLogger(DocCategoryResource.class);

    private static final String ENTITY_NAME = "docCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocCategoryService docCategoryService;

    private final DocCategoryRepository docCategoryRepository;

    private final DocCategoryQueryService docCategoryQueryService;

    public DocCategoryResource(
        DocCategoryService docCategoryService,
        DocCategoryRepository docCategoryRepository,
        DocCategoryQueryService docCategoryQueryService
    ) {
        this.docCategoryService = docCategoryService;
        this.docCategoryRepository = docCategoryRepository;
        this.docCategoryQueryService = docCategoryQueryService;
    }

    /**
     * {@code POST  /doc-categories} : Create a new docCategory.
     *
     * @param docCategory the docCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docCategory, or with status {@code 400 (Bad Request)} if the docCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doc-categories")
    public ResponseEntity<DocCategory> createDocCategory(@RequestBody DocCategory docCategory) throws URISyntaxException {
        log.debug("REST request to save DocCategory : {}", docCategory);
        if (docCategory.getId() != null) {
            throw new BadRequestAlertException("A new docCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocCategory result = docCategoryService.save(docCategory);
        return ResponseEntity
            .created(new URI("/api/doc-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doc-categories/:id} : Updates an existing docCategory.
     *
     * @param id the id of the docCategory to save.
     * @param docCategory the docCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docCategory,
     * or with status {@code 400 (Bad Request)} if the docCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doc-categories/{id}")
    public ResponseEntity<DocCategory> updateDocCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocCategory docCategory
    ) throws URISyntaxException {
        log.debug("REST request to update DocCategory : {}, {}", id, docCategory);
        if (docCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocCategory result = docCategoryService.update(docCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doc-categories/:id} : Partial updates given fields of an existing docCategory, field will ignore if it is null
     *
     * @param id the id of the docCategory to save.
     * @param docCategory the docCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docCategory,
     * or with status {@code 400 (Bad Request)} if the docCategory is not valid,
     * or with status {@code 404 (Not Found)} if the docCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the docCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doc-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocCategory> partialUpdateDocCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocCategory docCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocCategory partially : {}, {}", id, docCategory);
        if (docCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, docCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocCategory> result = docCategoryService.partialUpdate(docCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, docCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /doc-categories} : get all the docCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docCategories in body.
     */
    @GetMapping("/doc-categories")
    public ResponseEntity<List<DocCategory>> getAllDocCategories(
        DocCategoryCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DocCategories by criteria: {}", criteria);
        Page<DocCategory> page = docCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doc-categories/count} : count all the docCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doc-categories/count")
    public ResponseEntity<Long> countDocCategories(DocCategoryCriteria criteria) {
        log.debug("REST request to count DocCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(docCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /doc-categories/:id} : get the "id" docCategory.
     *
     * @param id the id of the docCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doc-categories/{id}")
    public ResponseEntity<DocCategory> getDocCategory(@PathVariable Long id) {
        log.debug("REST request to get DocCategory : {}", id);
        Optional<DocCategory> docCategory = docCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docCategory);
    }

    /**
     * {@code DELETE  /doc-categories/:id} : delete the "id" docCategory.
     *
     * @param id the id of the docCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doc-categories/{id}")
    public ResponseEntity<Void> deleteDocCategory(@PathVariable Long id) {
        log.debug("REST request to delete DocCategory : {}", id);
        docCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
