package com.perso.mydocs.service;

import com.perso.mydocs.domain.DocTopic;
import com.perso.mydocs.repository.DocTopicRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocTopic}.
 */
@Service
@Transactional
public class DocTopicService {

    private final Logger log = LoggerFactory.getLogger(DocTopicService.class);

    private final DocTopicRepository docTopicRepository;

    public DocTopicService(DocTopicRepository docTopicRepository) {
        this.docTopicRepository = docTopicRepository;
    }

    /**
     * Save a docTopic.
     *
     * @param docTopic the entity to save.
     * @return the persisted entity.
     */
    public DocTopic save(DocTopic docTopic) {
        log.debug("Request to save DocTopic : {}", docTopic);
        return docTopicRepository.save(docTopic);
    }

    /**
     * Update a docTopic.
     *
     * @param docTopic the entity to save.
     * @return the persisted entity.
     */
    public DocTopic update(DocTopic docTopic) {
        log.debug("Request to update DocTopic : {}", docTopic);
        return docTopicRepository.save(docTopic);
    }

    /**
     * Partially update a docTopic.
     *
     * @param docTopic the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocTopic> partialUpdate(DocTopic docTopic) {
        log.debug("Request to partially update DocTopic : {}", docTopic);

        return docTopicRepository
            .findById(docTopic.getId())
            .map(existingDocTopic -> {
                if (docTopic.getName() != null) {
                    existingDocTopic.setName(docTopic.getName());
                }
                if (docTopic.getCode() != null) {
                    existingDocTopic.setCode(docTopic.getCode());
                }
                if (docTopic.getImgUrl() != null) {
                    existingDocTopic.setImgUrl(docTopic.getImgUrl());
                }
                if (docTopic.getNotes() != null) {
                    existingDocTopic.setNotes(docTopic.getNotes());
                }

                return existingDocTopic;
            })
            .map(docTopicRepository::save);
    }

    /**
     * Get all the docTopics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocTopic> findAll(Pageable pageable) {
        log.debug("Request to get all DocTopics");
        return docTopicRepository.findAll(pageable);
    }

    /**
     * Get all the docTopics with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DocTopic> findAllWithEagerRelationships(Pageable pageable) {
        return docTopicRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one docTopic by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocTopic> findOne(Long id) {
        log.debug("Request to get DocTopic : {}", id);
        return docTopicRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the docTopic by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocTopic : {}", id);
        docTopicRepository.deleteById(id);
    }
}
