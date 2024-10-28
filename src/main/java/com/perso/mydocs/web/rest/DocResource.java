package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.repository.DocRepository;
import com.perso.mydocs.service.DocQueryService;
import com.perso.mydocs.service.DocService;
import com.perso.mydocs.service.criteria.DocCriteria;
import com.perso.mydocs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.perso.mydocs.domain.Doc}.
 */
@RestController
@RequestMapping("/api")
public class DocResource {

    private final Logger log = LoggerFactory.getLogger(DocResource.class);

    private static final String ENTITY_NAME = "doc";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocService docService;

    private final DocRepository docRepository;

    private final DocQueryService docQueryService;

    public DocResource(DocService docService, DocRepository docRepository, DocQueryService docQueryService) {
        this.docService = docService;
        this.docRepository = docRepository;
        this.docQueryService = docQueryService;
    }

    /**
     * {@code POST  /docs} : Create a new doc.
     *
     * @param doc the doc to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doc, or with status {@code 400 (Bad Request)} if the doc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/docs")
    public ResponseEntity<Doc> createDoc(@Valid @RequestBody Doc doc) throws URISyntaxException {
        log.debug("REST request to save Doc : {}", doc);
        if (doc.getId() != null) {
            throw new BadRequestAlertException("A new doc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Doc result = docService.save(doc);
        return ResponseEntity
            .created(new URI("/api/docs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /docs/:id} : Updates an existing doc.
     *
     * @param id the id of the doc to save.
     * @param doc the doc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doc,
     * or with status {@code 400 (Bad Request)} if the doc is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/docs/{id}")
    public ResponseEntity<Doc> updateDoc(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Doc doc)
        throws URISyntaxException {
        log.debug("REST request to update Doc : {}, {}", id, doc);
        if (doc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Doc result = docService.update(doc);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, doc.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /docs/:id} : Partial updates given fields of an existing doc, field will ignore if it is null
     *
     * @param id the id of the doc to save.
     * @param doc the doc to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doc,
     * or with status {@code 400 (Bad Request)} if the doc is not valid,
     * or with status {@code 404 (Not Found)} if the doc is not found,
     * or with status {@code 500 (Internal Server Error)} if the doc couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/docs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Doc> partialUpdateDoc(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Doc doc)
        throws URISyntaxException {
        log.debug("REST request to partial update Doc partially : {}, {}", id, doc);
        if (doc.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doc.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!docRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Doc> result = docService.partialUpdate(doc);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, doc.getId().toString())
        );
    }

    /**
     * {@code GET  /docs} : get all the docs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docs in body.
     */
    @GetMapping("/docs")
    public ResponseEntity<List<Doc>> getAllDocs(DocCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Docs by criteria: {}", criteria);
        Page<Doc> page = docQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /docs/count} : count all the docs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/docs/count")
    public ResponseEntity<Long> countDocs(DocCriteria criteria) {
        log.debug("REST request to count Docs by criteria: {}", criteria);
        return ResponseEntity.ok().body(docQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /docs/:id} : get the "id" doc.
     *
     * @param id the id of the doc to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doc, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/docs/{id}")
    public ResponseEntity<Doc> getDoc(@PathVariable Long id) {
        log.debug("REST request to get Doc : {}", id);
        System.out.println("********************* DocResource.getDoc() ******************");
        Optional<Doc> doc = docService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doc);
    }

    /**
     * {@code DELETE  /docs/:id} : delete the "id" doc.
     *
     * @param id the id of the doc to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/docs/{id}")
    public ResponseEntity<Void> deleteDoc(@PathVariable Long id) {
        log.debug("REST request to delete Doc : {}", id);
        docService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
