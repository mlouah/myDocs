package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.Doc;
import com.perso.mydocs.repository.DocRepository;
import com.perso.mydocs.service.criteria.DocCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Doc} entities in the database.
 * The main input is a {@link DocCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Doc} or a {@link Page} of {@link Doc} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocQueryService extends QueryService<Doc> {

    private final Logger log = LoggerFactory.getLogger(DocQueryService.class);

    private final DocRepository docRepository;

    public DocQueryService(DocRepository docRepository) {
        this.docRepository = docRepository;
    }

    /**
     * Return a {@link List} of {@link Doc} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Doc> findByCriteria(DocCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Doc> specification = createSpecification(criteria);
        return docRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Doc} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Doc> findByCriteria(DocCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Doc> specification = createSpecification(criteria);
        return docRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Doc> specification = createSpecification(criteria);
        return docRepository.count(specification);
    }

    /**
     * Function to convert {@link DocCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Doc> createSpecification(DocCriteria criteria) {
        Specification<Doc> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Doc_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Doc_.title));
            }
            if (criteria.getSubTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubTitle(), Doc_.subTitle));
            }
            if (criteria.getPublishYear() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPublishYear(), Doc_.publishYear));
            }
            if (criteria.getCoverImgPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImgPath(), Doc_.coverImgPath));
            }
            if (criteria.getEditionNumer() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEditionNumer(), Doc_.editionNumer));
            }
            if (criteria.getPurchaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseDate(), Doc_.purchaseDate));
            }
            if (criteria.getStartReadingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartReadingDate(), Doc_.startReadingDate));
            }
            if (criteria.getEndReadingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndReadingDate(), Doc_.endReadingDate));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Doc_.price));
            }
            if (criteria.getCopies() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCopies(), Doc_.copies));
            }
            if (criteria.getPageNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageNumber(), Doc_.pageNumber));
            }
            if (criteria.getNumDoc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumDoc(), Doc_.numDoc));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilename(), Doc_.filename));
            }
            if (criteria.getPublisherId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPublisherId(),
                            root -> root.join(Doc_.publisher, JoinType.LEFT).get(DocPublisher_.id)
                        )
                    );
            }
            if (criteria.getCollectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCollectionId(),
                            root -> root.join(Doc_.collection, JoinType.LEFT).get(DocCollection_.id)
                        )
                    );
            }
            if (criteria.getFormatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFormatId(), root -> root.join(Doc_.format, JoinType.LEFT).get(DocFormat_.id))
                    );
            }
            if (criteria.getLangueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLangueId(), root -> root.join(Doc_.langue, JoinType.LEFT).get(Language_.id))
                    );
            }
            if (criteria.getMaintopicId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMaintopicId(), root -> root.join(Doc_.maintopic, JoinType.LEFT).get(DocTopic_.id))
                    );
            }
            if (criteria.getMainAuthorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMainAuthorId(), root -> root.join(Doc_.mainAuthor, JoinType.LEFT).get(DocAuthor_.id))
                    );
            }
            if (criteria.getDocCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocCategoryId(),
                            root -> root.join(Doc_.docCategory, JoinType.LEFT).get(DocCategory_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
