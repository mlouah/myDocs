package com.perso.mydocs.service;

import com.perso.mydocs.domain.DocCollection;
import com.perso.mydocs.repository.DocCollectionRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocCollection}.
 */
@Service
@Transactional
public class DocCollectionService {

    private final Logger log = LoggerFactory.getLogger(DocCollectionService.class);

    private final DocCollectionRepository docCollectionRepository;

    public DocCollectionService(DocCollectionRepository docCollectionRepository) {
        this.docCollectionRepository = docCollectionRepository;
    }

    /**
     * Save a docCollection.
     *
     * @param docCollection the entity to save.
     * @return the persisted entity.
     */
    public DocCollection save(DocCollection docCollection) {
        log.debug("Request to save DocCollection : {}", docCollection);
        return docCollectionRepository.save(docCollection);
    }

    /**
     * Update a docCollection.
     *
     * @param docCollection the entity to save.
     * @return the persisted entity.
     */
    public DocCollection update(DocCollection docCollection) {
        log.debug("Request to update DocCollection : {}", docCollection);
        return docCollectionRepository.save(docCollection);
    }

    /**
     * Partially update a docCollection.
     *
     * @param docCollection the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocCollection> partialUpdate(DocCollection docCollection) {
        log.debug("Request to partially update DocCollection : {}", docCollection);

        return docCollectionRepository
            .findById(docCollection.getId())
            .map(existingDocCollection -> {
                if (docCollection.getName() != null) {
                    existingDocCollection.setName(docCollection.getName());
                }
                if (docCollection.getNotes() != null) {
                    existingDocCollection.setNotes(docCollection.getNotes());
                }

                return existingDocCollection;
            })
            .map(docCollectionRepository::save);
    }

    /**
     * Get all the docCollections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocCollection> findAll(Pageable pageable) {
        log.debug("Request to get all DocCollections");
        return docCollectionRepository.findAll(pageable);
    }

    /**
     * Get all the docCollections with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DocCollection> findAllWithEagerRelationships(Pageable pageable) {
        return docCollectionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the docCollections where Doc is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocCollection> findAllWhereDocIsNull() {
        log.debug("Request to get all docCollections where Doc is null");
        return StreamSupport
            .stream(docCollectionRepository.findAll().spliterator(), false)
            .filter(docCollection -> docCollection.getDoc() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one docCollection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocCollection> findOne(Long id) {
        log.debug("Request to get DocCollection : {}", id);
        return docCollectionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the docCollection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocCollection : {}", id);
        docCollectionRepository.deleteById(id);
    }
}
