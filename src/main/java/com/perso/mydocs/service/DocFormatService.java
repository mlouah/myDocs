package com.perso.mydocs.service;

import com.perso.mydocs.domain.DocFormat;
import com.perso.mydocs.repository.DocFormatRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocFormat}.
 */
@Service
@Transactional
public class DocFormatService {

    private final Logger log = LoggerFactory.getLogger(DocFormatService.class);

    private final DocFormatRepository docFormatRepository;

    public DocFormatService(DocFormatRepository docFormatRepository) {
        this.docFormatRepository = docFormatRepository;
    }

    /**
     * Save a docFormat.
     *
     * @param docFormat the entity to save.
     * @return the persisted entity.
     */
    public DocFormat save(DocFormat docFormat) {
        log.debug("Request to save DocFormat : {}", docFormat);
        return docFormatRepository.save(docFormat);
    }

    /**
     * Update a docFormat.
     *
     * @param docFormat the entity to save.
     * @return the persisted entity.
     */
    public DocFormat update(DocFormat docFormat) {
        log.debug("Request to update DocFormat : {}", docFormat);
        return docFormatRepository.save(docFormat);
    }

    /**
     * Partially update a docFormat.
     *
     * @param docFormat the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocFormat> partialUpdate(DocFormat docFormat) {
        log.debug("Request to partially update DocFormat : {}", docFormat);

        return docFormatRepository
            .findById(docFormat.getId())
            .map(existingDocFormat -> {
                if (docFormat.getFormat() != null) {
                    existingDocFormat.setFormat(docFormat.getFormat());
                }
                if (docFormat.getCode() != null) {
                    existingDocFormat.setCode(docFormat.getCode());
                }
                if (docFormat.getNotes() != null) {
                    existingDocFormat.setNotes(docFormat.getNotes());
                }

                return existingDocFormat;
            })
            .map(docFormatRepository::save);
    }

    /**
     * Get all the docFormats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocFormat> findAll(Pageable pageable) {
        log.debug("Request to get all DocFormats");
        return docFormatRepository.findAll(pageable);
    }

    /**
     * Get one docFormat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocFormat> findOne(Long id) {
        log.debug("Request to get DocFormat : {}", id);
        return docFormatRepository.findById(id);
    }

    /**
     * Delete the docFormat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocFormat : {}", id);
        docFormatRepository.deleteById(id);
    }
}
