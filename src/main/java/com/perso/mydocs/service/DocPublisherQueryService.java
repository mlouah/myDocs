package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.DocPublisher;
import com.perso.mydocs.repository.DocPublisherRepository;
import com.perso.mydocs.service.criteria.DocPublisherCriteria;
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
 * Service for executing complex queries for {@link DocPublisher} entities in the database.
 * The main input is a {@link DocPublisherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocPublisher} or a {@link Page} of {@link DocPublisher} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocPublisherQueryService extends QueryService<DocPublisher> {

    private final Logger log = LoggerFactory.getLogger(DocPublisherQueryService.class);

    private final DocPublisherRepository docPublisherRepository;

    public DocPublisherQueryService(DocPublisherRepository docPublisherRepository) {
        this.docPublisherRepository = docPublisherRepository;
    }

    /**
     * Return a {@link List} of {@link DocPublisher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocPublisher> findByCriteria(DocPublisherCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocPublisher> specification = createSpecification(criteria);
        return docPublisherRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocPublisher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocPublisher> findByCriteria(DocPublisherCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocPublisher> specification = createSpecification(criteria);
        return docPublisherRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocPublisherCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocPublisher> specification = createSpecification(criteria);
        return docPublisherRepository.count(specification);
    }

    /**
     * Function to convert {@link DocPublisherCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocPublisher> createSpecification(DocPublisherCriteria criteria) {
        Specification<DocPublisher> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocPublisher_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DocPublisher_.name));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), DocPublisher_.url));
            }
            if (criteria.getDocId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDocId(), root -> root.join(DocPublisher_.docs, JoinType.LEFT).get(Doc_.id))
                    );
            }
            if (criteria.getCollectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCollectionId(),
                            root -> root.join(DocPublisher_.collections, JoinType.LEFT).get(DocCollection_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
