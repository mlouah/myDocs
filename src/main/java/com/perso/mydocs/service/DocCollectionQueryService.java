package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.DocCollection;
import com.perso.mydocs.repository.DocCollectionRepository;
import com.perso.mydocs.service.criteria.DocCollectionCriteria;
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
 * Service for executing complex queries for {@link DocCollection} entities in the database.
 * The main input is a {@link DocCollectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocCollection} or a {@link Page} of {@link DocCollection} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocCollectionQueryService extends QueryService<DocCollection> {

    private final Logger log = LoggerFactory.getLogger(DocCollectionQueryService.class);

    private final DocCollectionRepository docCollectionRepository;

    public DocCollectionQueryService(DocCollectionRepository docCollectionRepository) {
        this.docCollectionRepository = docCollectionRepository;
    }

    /**
     * Return a {@link List} of {@link DocCollection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocCollection> findByCriteria(DocCollectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocCollection> specification = createSpecification(criteria);
        return docCollectionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocCollection} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocCollection> findByCriteria(DocCollectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocCollection> specification = createSpecification(criteria);
        return docCollectionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocCollectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocCollection> specification = createSpecification(criteria);
        return docCollectionRepository.count(specification);
    }

    /**
     * Function to convert {@link DocCollectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocCollection> createSpecification(DocCollectionCriteria criteria) {
        Specification<DocCollection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocCollection_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DocCollection_.name));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), DocCollection_.notes));
            }
            if (criteria.getDocId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDocId(), root -> root.join(DocCollection_.docs, JoinType.LEFT).get(Doc_.id))
                    );
            }
            if (criteria.getDocPublisherId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocPublisherId(),
                            root -> root.join(DocCollection_.docPublisher, JoinType.LEFT).get(DocPublisher_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
