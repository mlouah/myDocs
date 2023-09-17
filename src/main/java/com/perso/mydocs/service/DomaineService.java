package com.perso.mydocs.service;

import com.perso.mydocs.domain.Domaine;
import com.perso.mydocs.repository.DomaineRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Domaine}.
 */
@Service
@Transactional
public class DomaineService {

    private final Logger log = LoggerFactory.getLogger(DomaineService.class);

    private final DomaineRepository domaineRepository;

    public DomaineService(DomaineRepository domaineRepository) {
        this.domaineRepository = domaineRepository;
    }

    /**
     * Save a domaine.
     *
     * @param domaine the entity to save.
     * @return the persisted entity.
     */
    public Domaine save(Domaine domaine) {
        log.debug("Request to save Domaine : {}", domaine);
        return domaineRepository.save(domaine);
    }

    /**
     * Update a domaine.
     *
     * @param domaine the entity to save.
     * @return the persisted entity.
     */
    public Domaine update(Domaine domaine) {
        log.debug("Request to update Domaine : {}", domaine);
        return domaineRepository.save(domaine);
    }

    /**
     * Partially update a domaine.
     *
     * @param domaine the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Domaine> partialUpdate(Domaine domaine) {
        log.debug("Request to partially update Domaine : {}", domaine);

        return domaineRepository
            .findById(domaine.getId())
            .map(existingDomaine -> {
                if (domaine.getName() != null) {
                    existingDomaine.setName(domaine.getName());
                }
                if (domaine.getCode() != null) {
                    existingDomaine.setCode(domaine.getCode());
                }
                if (domaine.getNotes() != null) {
                    existingDomaine.setNotes(domaine.getNotes());
                }

                return existingDomaine;
            })
            .map(domaineRepository::save);
    }

    /**
     * Get all the domaines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Domaine> findAll(Pageable pageable) {
        log.debug("Request to get all Domaines");
        return domaineRepository.findAll(pageable);
    }

    /**
     * Get one domaine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Domaine> findOne(Long id) {
        log.debug("Request to get Domaine : {}", id);
        return domaineRepository.findById(id);
    }

    /**
     * Delete the domaine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Domaine : {}", id);
        domaineRepository.deleteById(id);
    }
}
