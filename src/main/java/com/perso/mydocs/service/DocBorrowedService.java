package com.perso.mydocs.service;

import com.perso.mydocs.domain.DocBorrowed;
import com.perso.mydocs.repository.DocBorrowedRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocBorrowed}.
 */
@Service
@Transactional
public class DocBorrowedService {

    private final Logger log = LoggerFactory.getLogger(DocBorrowedService.class);

    private final DocBorrowedRepository docBorrowedRepository;

    public DocBorrowedService(DocBorrowedRepository docBorrowedRepository) {
        this.docBorrowedRepository = docBorrowedRepository;
    }

    /**
     * Save a docBorrowed.
     *
     * @param docBorrowed the entity to save.
     * @return the persisted entity.
     */
    public DocBorrowed save(DocBorrowed docBorrowed) {
        log.debug("Request to save DocBorrowed : {}", docBorrowed);
        return docBorrowedRepository.save(docBorrowed);
    }

    /**
     * Update a docBorrowed.
     *
     * @param docBorrowed the entity to save.
     * @return the persisted entity.
     */
    public DocBorrowed update(DocBorrowed docBorrowed) {
        log.debug("Request to update DocBorrowed : {}", docBorrowed);
        return docBorrowedRepository.save(docBorrowed);
    }

    /**
     * Partially update a docBorrowed.
     *
     * @param docBorrowed the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocBorrowed> partialUpdate(DocBorrowed docBorrowed) {
        log.debug("Request to partially update DocBorrowed : {}", docBorrowed);

        return docBorrowedRepository
            .findById(docBorrowed.getId())
            .map(existingDocBorrowed -> {
                if (docBorrowed.getBorrowDate() != null) {
                    existingDocBorrowed.setBorrowDate(docBorrowed.getBorrowDate());
                }
                if (docBorrowed.getBorrowerName() != null) {
                    existingDocBorrowed.setBorrowerName(docBorrowed.getBorrowerName());
                }
                if (docBorrowed.getNotes() != null) {
                    existingDocBorrowed.setNotes(docBorrowed.getNotes());
                }

                return existingDocBorrowed;
            })
            .map(docBorrowedRepository::save);
    }

    /**
     * Get all the docBorroweds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocBorrowed> findAll(Pageable pageable) {
        log.debug("Request to get all DocBorroweds");
        return docBorrowedRepository.findAll(pageable);
    }

    /**
     * Get all the docBorroweds with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DocBorrowed> findAllWithEagerRelationships(Pageable pageable) {
        return docBorrowedRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one docBorrowed by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocBorrowed> findOne(Long id) {
        log.debug("Request to get DocBorrowed : {}", id);
        return docBorrowedRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the docBorrowed by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocBorrowed : {}", id);
        docBorrowedRepository.deleteById(id);
    }
}
