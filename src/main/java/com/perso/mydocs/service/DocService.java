package com.perso.mydocs.service;

import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.repository.DocRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Doc}.
 */
@Service
@Transactional
public class DocService {

    private final Logger log = LoggerFactory.getLogger(DocService.class);

    private final DocRepository docRepository;

    public DocService(DocRepository docRepository) {
        this.docRepository = docRepository;
    }

    /**
     * Save a doc.
     *
     * @param doc the entity to save.
     * @return the persisted entity.
     */
    public Doc save(Doc doc) {
        log.debug("Request to save Doc : {}", doc);
        return docRepository.save(doc);
    }

    /**
     * Update a doc.
     *
     * @param doc the entity to save.
     * @return the persisted entity.
     */
    public Doc update(Doc doc) {
        log.debug("Request to update Doc : {}", doc);
        return docRepository.save(doc);
    }

    /**
     * Partially update a doc.
     *
     * @param doc the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Doc> partialUpdate(Doc doc) {
        log.debug("Request to partially update Doc : {}", doc);

        return docRepository
            .findById(doc.getId())
            .map(existingDoc -> {
                if (doc.getTitle() != null) {
                    existingDoc.setTitle(doc.getTitle());
                }
                if (doc.getSubTitle() != null) {
                    existingDoc.setSubTitle(doc.getSubTitle());
                }
                if (doc.getPublishYear() != null) {
                    existingDoc.setPublishYear(doc.getPublishYear());
                }
                if (doc.getCoverImgPath() != null) {
                    existingDoc.setCoverImgPath(doc.getCoverImgPath());
                }
                if (doc.getEditionNumer() != null) {
                    existingDoc.setEditionNumer(doc.getEditionNumer());
                }
                if (doc.getSummary() != null) {
                    existingDoc.setSummary(doc.getSummary());
                }
                if (doc.getPurchaseDate() != null) {
                    existingDoc.setPurchaseDate(doc.getPurchaseDate());
                }
                if (doc.getStartReadingDate() != null) {
                    existingDoc.setStartReadingDate(doc.getStartReadingDate());
                }
                if (doc.getEndReadingDate() != null) {
                    existingDoc.setEndReadingDate(doc.getEndReadingDate());
                }
                if (doc.getPrice() != null) {
                    existingDoc.setPrice(doc.getPrice());
                }
                if (doc.getCopies() != null) {
                    existingDoc.setCopies(doc.getCopies());
                }
                if (doc.getPageNumber() != null) {
                    existingDoc.setPageNumber(doc.getPageNumber());
                }
                if (doc.getNumDoc() != null) {
                    existingDoc.setNumDoc(doc.getNumDoc());
                }
                if (doc.getMyNotes() != null) {
                    existingDoc.setMyNotes(doc.getMyNotes());
                }
                if (doc.getKeywords() != null) {
                    existingDoc.setKeywords(doc.getKeywords());
                }
                if (doc.getToc() != null) {
                    existingDoc.setToc(doc.getToc());
                }
                if (doc.getFilename() != null) {
                    existingDoc.setFilename(doc.getFilename());
                }

                return existingDoc;
            })
            .map(docRepository::save);
    }

    /**
     * Get all the docs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Doc> findAll(Pageable pageable) {
        log.debug("Request to get all Docs");
        return docRepository.findAll(pageable);
    }

    /**
     * Get all the docs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Doc> findAllWithEagerRelationships(Pageable pageable) {
        return docRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one doc by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Doc> findOne(Long id) {
        log.debug("Request to get Doc : {}", id);
        return docRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the doc by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Doc : {}", id);
        docRepository.deleteById(id);
    }
}
