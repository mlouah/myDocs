package com.perso.mydocs.service;

import com.perso.mydocs.domain.DocPublisher;
import com.perso.mydocs.repository.DocPublisherRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocPublisher}.
 */
@Service
@Transactional
public class DocPublisherService {

    private final Logger log = LoggerFactory.getLogger(DocPublisherService.class);

    private final DocPublisherRepository docPublisherRepository;

    public DocPublisherService(DocPublisherRepository docPublisherRepository) {
        this.docPublisherRepository = docPublisherRepository;
    }

    /**
     * Save a docPublisher.
     *
     * @param docPublisher the entity to save.
     * @return the persisted entity.
     */
    public DocPublisher save(DocPublisher docPublisher) {
        log.debug("Request to save DocPublisher : {}", docPublisher);
        return docPublisherRepository.save(docPublisher);
    }

    /**
     * Update a docPublisher.
     *
     * @param docPublisher the entity to save.
     * @return the persisted entity.
     */
    public DocPublisher update(DocPublisher docPublisher) {
        log.debug("Request to update DocPublisher : {}", docPublisher);
        return docPublisherRepository.save(docPublisher);
    }

    /**
     * Partially update a docPublisher.
     *
     * @param docPublisher the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocPublisher> partialUpdate(DocPublisher docPublisher) {
        log.debug("Request to partially update DocPublisher : {}", docPublisher);

        return docPublisherRepository
            .findById(docPublisher.getId())
            .map(existingDocPublisher -> {
                if (docPublisher.getName() != null) {
                    existingDocPublisher.setName(docPublisher.getName());
                }
                if (docPublisher.getNotes() != null) {
                    existingDocPublisher.setNotes(docPublisher.getNotes());
                }

                return existingDocPublisher;
            })
            .map(docPublisherRepository::save);
    }

    /**
     * Get all the docPublishers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocPublisher> findAll(Pageable pageable) {
        log.debug("Request to get all DocPublishers");
        return docPublisherRepository.findAll(pageable);
    }

    /**
     * Get one docPublisher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocPublisher> findOne(Long id) {
        log.debug("Request to get DocPublisher : {}", id);
        return docPublisherRepository.findById(id);
    }

    /**
     * Delete the docPublisher by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocPublisher : {}", id);
        docPublisherRepository.deleteById(id);
    }
}
