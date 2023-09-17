package com.perso.mydocs.service;

import com.perso.mydocs.domain.DocCategory;
import com.perso.mydocs.repository.DocCategoryRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocCategory}.
 */
@Service
@Transactional
public class DocCategoryService {

    private final Logger log = LoggerFactory.getLogger(DocCategoryService.class);

    private final DocCategoryRepository docCategoryRepository;

    public DocCategoryService(DocCategoryRepository docCategoryRepository) {
        this.docCategoryRepository = docCategoryRepository;
    }

    /**
     * Save a docCategory.
     *
     * @param docCategory the entity to save.
     * @return the persisted entity.
     */
    public DocCategory save(DocCategory docCategory) {
        log.debug("Request to save DocCategory : {}", docCategory);
        return docCategoryRepository.save(docCategory);
    }

    /**
     * Update a docCategory.
     *
     * @param docCategory the entity to save.
     * @return the persisted entity.
     */
    public DocCategory update(DocCategory docCategory) {
        log.debug("Request to update DocCategory : {}", docCategory);
        return docCategoryRepository.save(docCategory);
    }

    /**
     * Partially update a docCategory.
     *
     * @param docCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocCategory> partialUpdate(DocCategory docCategory) {
        log.debug("Request to partially update DocCategory : {}", docCategory);

        return docCategoryRepository
            .findById(docCategory.getId())
            .map(existingDocCategory -> {
                if (docCategory.getName() != null) {
                    existingDocCategory.setName(docCategory.getName());
                }
                if (docCategory.getCode() != null) {
                    existingDocCategory.setCode(docCategory.getCode());
                }
                if (docCategory.getNotes() != null) {
                    existingDocCategory.setNotes(docCategory.getNotes());
                }

                return existingDocCategory;
            })
            .map(docCategoryRepository::save);
    }

    /**
     * Get all the docCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocCategory> findAll(Pageable pageable) {
        log.debug("Request to get all DocCategories");
        return docCategoryRepository.findAll(pageable);
    }

    /**
     * Get one docCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocCategory> findOne(Long id) {
        log.debug("Request to get DocCategory : {}", id);
        return docCategoryRepository.findById(id);
    }

    /**
     * Delete the docCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DocCategory : {}", id);
        docCategoryRepository.deleteById(id);
    }
}
