package com.perso.mydocs.web.rest;

import com.perso.mydocs.domain.Domaine;
import com.perso.mydocs.repository.DomaineRepository;
import com.perso.mydocs.service.DomaineQueryService;
import com.perso.mydocs.service.DomaineService;
import com.perso.mydocs.service.criteria.DomaineCriteria;
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
 * REST controller for managing {@link com.perso.mydocs.domain.Domaine}.
 */
@RestController
@RequestMapping("/api")
public class DomaineResource {

    private final Logger log = LoggerFactory.getLogger(DomaineResource.class);

    private static final String ENTITY_NAME = "domaine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomaineService domaineService;

    private final DomaineRepository domaineRepository;

    private final DomaineQueryService domaineQueryService;

    public DomaineResource(DomaineService domaineService, DomaineRepository domaineRepository, DomaineQueryService domaineQueryService) {
        this.domaineService = domaineService;
        this.domaineRepository = domaineRepository;
        this.domaineQueryService = domaineQueryService;
    }

    /**
     * {@code POST  /domaines} : Create a new domaine.
     *
     * @param domaine the domaine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domaine, or with status {@code 400 (Bad Request)} if the domaine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/domaines")
    public ResponseEntity<Domaine> createDomaine(@RequestBody Domaine domaine) throws URISyntaxException {
        log.debug("REST request to save Domaine : {}", domaine);
        if (domaine.getId() != null) {
            throw new BadRequestAlertException("A new domaine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Domaine result = domaineService.save(domaine);
        return ResponseEntity
            .created(new URI("/api/domaines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /domaines/:id} : Updates an existing domaine.
     *
     * @param id the id of the domaine to save.
     * @param domaine the domaine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaine,
     * or with status {@code 400 (Bad Request)} if the domaine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domaine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/domaines/{id}")
    public ResponseEntity<Domaine> updateDomaine(@PathVariable(value = "id", required = false) final Long id, @RequestBody Domaine domaine)
        throws URISyntaxException {
        log.debug("REST request to update Domaine : {}, {}", id, domaine);
        if (domaine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Domaine result = domaineService.update(domaine);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, domaine.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /domaines/:id} : Partial updates given fields of an existing domaine, field will ignore if it is null
     *
     * @param id the id of the domaine to save.
     * @param domaine the domaine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaine,
     * or with status {@code 400 (Bad Request)} if the domaine is not valid,
     * or with status {@code 404 (Not Found)} if the domaine is not found,
     * or with status {@code 500 (Internal Server Error)} if the domaine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/domaines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Domaine> partialUpdateDomaine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Domaine domaine
    ) throws URISyntaxException {
        log.debug("REST request to partial update Domaine partially : {}, {}", id, domaine);
        if (domaine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Domaine> result = domaineService.partialUpdate(domaine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, domaine.getId().toString())
        );
    }

    /**
     * {@code GET  /domaines} : get all the domaines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domaines in body.
     */
    @GetMapping("/domaines")
    public ResponseEntity<List<Domaine>> getAllDomaines(
        DomaineCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Domaines by criteria: {}", criteria);
        Page<Domaine> page = domaineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /domaines/count} : count all the domaines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/domaines/count")
    public ResponseEntity<Long> countDomaines(DomaineCriteria criteria) {
        log.debug("REST request to count Domaines by criteria: {}", criteria);
        return ResponseEntity.ok().body(domaineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /domaines/:id} : get the "id" domaine.
     *
     * @param id the id of the domaine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domaine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/domaines/{id}")
    public ResponseEntity<Domaine> getDomaine(@PathVariable Long id) {
        log.debug("REST request to get Domaine : {}", id);
        Optional<Domaine> domaine = domaineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(domaine);
    }

    /**
     * {@code DELETE  /domaines/:id} : delete the "id" domaine.
     *
     * @param id the id of the domaine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/domaines/{id}")
    public ResponseEntity<Void> deleteDomaine(@PathVariable Long id) {
        log.debug("REST request to delete Domaine : {}", id);
        domaineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
