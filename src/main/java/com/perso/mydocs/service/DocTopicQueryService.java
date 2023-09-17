package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.DocTopic;
import com.perso.mydocs.repository.DocTopicRepository;
import com.perso.mydocs.service.criteria.DocTopicCriteria;
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
 * Service for executing complex queries for {@link DocTopic} entities in the database.
 * The main input is a {@link DocTopicCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocTopic} or a {@link Page} of {@link DocTopic} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocTopicQueryService extends QueryService<DocTopic> {

    private final Logger log = LoggerFactory.getLogger(DocTopicQueryService.class);

    private final DocTopicRepository docTopicRepository;

    public DocTopicQueryService(DocTopicRepository docTopicRepository) {
        this.docTopicRepository = docTopicRepository;
    }

    /**
     * Return a {@link List} of {@link DocTopic} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocTopic> findByCriteria(DocTopicCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocTopic> specification = createSpecification(criteria);
        return docTopicRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocTopic} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocTopic> findByCriteria(DocTopicCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocTopic> specification = createSpecification(criteria);
        return docTopicRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocTopicCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocTopic> specification = createSpecification(criteria);
        return docTopicRepository.count(specification);
    }

    /**
     * Function to convert {@link DocTopicCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocTopic> createSpecification(DocTopicCriteria criteria) {
        Specification<DocTopic> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocTopic_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DocTopic_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), DocTopic_.code));
            }
            if (criteria.getImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgUrl(), DocTopic_.imgUrl));
            }
            if (criteria.getDocId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDocId(), root -> root.join(DocTopic_.docs, JoinType.LEFT).get(Doc_.id))
                    );
            }
            if (criteria.getDomaineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDomaineId(), root -> root.join(DocTopic_.domaine, JoinType.LEFT).get(Domaine_.id))
                    );
            }
        }
        return specification;
    }
}
