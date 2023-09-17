package com.perso.mydocs.service;

import com.perso.mydocs.domain.DocAuthor;
import com.perso.mydocs.repository.DocAuthorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocAuthor}.
 */
@Service
@Transactional
public class DocAuthorService {

    private final Logger log = LoggerFactory.getLogger(DocAuthorService.class);

    private final DocAuthorRepository docAuthorRepository;

    public DocAuthorService(DocAuthorRepository docAuthorRepository) {
        this.docAuthorRepository = docAuthorRepository;
    }

    /**
     * Save a docAuthor.
     *
     * @param docAuthor the entity to save.
     * @return the persisted entity.
     */
    public DocAuthor save(DocAuthor docAuthor) {
        log.debug("Request to save DocAuthor : {}", docAuthor);
        return docAuthorRepository.save(docAuthor);
    }

    /**
     * Update a docAuthor.
     *
     * @param docAuthor the entity to save.
     * @return the persisted entity.
     */
    public DocAuthor update(DocAuthor docAuthor) {
        log.debug("Request to update DocAuthor : {}", docAuthor);
        return docAuthorRepository.save(docAuthor);
    }

    /**
     * Partially update a docAuthor.
     *
     * @param docAuthor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocAuthor> partialUpdate(DocAuthor docAuthor) {
        log.debug("Request to partially update DocAuthor : {}", docAuthor);

        return docAuthorRepository
            .findById(docAuthor.getId())
            .map(existingDocAuthor -> {
                if (docAuthor.getName() != null) {
                    existingDocAuthor.setName(docAuthor.getName());
                }
                if (docAuthor.getImgUrl() != null) {
                    existingDocAuthor.setImgUrl(docAuthor.getImgUrl());
                }
                if (docAuthor.getNotes() != null) {
                    existingDocAuthor.setNotes(docAuthor.getNotes());
                }
                if (docAuthor.getUrl() != null) {
                    existingDocAuthor.setUrl(docAuthor.getUrl());
                }

                return existingDocAuthor;
            })
            .map(docAuthorRepository::save);
    }

    /**
     * Get all the docAuthors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocAuthor> findAll(Pageable pageable) {
        log.debug("Request to get all DocAuthors");
        return docAuthorRepository.findAll(pageable);
    }

    /**
     * Get one docAuthor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocAuthor> findOne(Long id) {
        log.debug("Request to get DocAuthor : {}", id);
        return docAuthorRepository.findById(id);
    }

    /**
     * Delete the docAuthor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocAuthor : {}", id);
        docAuthorRepository.deleteById(id);
    }
}
