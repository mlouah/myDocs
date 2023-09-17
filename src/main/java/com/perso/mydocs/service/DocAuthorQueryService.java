package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.DocAuthor;
import com.perso.mydocs.repository.DocAuthorRepository;
import com.perso.mydocs.service.criteria.DocAuthorCriteria;
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
 * Service for executing complex queries for {@link DocAuthor} entities in the database.
 * The main input is a {@link DocAuthorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocAuthor} or a {@link Page} of {@link DocAuthor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocAuthorQueryService extends QueryService<DocAuthor> {

    private final Logger log = LoggerFactory.getLogger(DocAuthorQueryService.class);

    private final DocAuthorRepository docAuthorRepository;

    public DocAuthorQueryService(DocAuthorRepository docAuthorRepository) {
        this.docAuthorRepository = docAuthorRepository;
    }

    /**
     * Return a {@link List} of {@link DocAuthor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocAuthor> findByCriteria(DocAuthorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocAuthor> specification = createSpecification(criteria);
        return docAuthorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocAuthor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocAuthor> findByCriteria(DocAuthorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocAuthor> specification = createSpecification(criteria);
        return docAuthorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocAuthorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocAuthor> specification = createSpecification(criteria);
        return docAuthorRepository.count(specification);
    }

    /**
     * Function to convert {@link DocAuthorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocAuthor> createSpecification(DocAuthorCriteria criteria) {
        Specification<DocAuthor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocAuthor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DocAuthor_.name));
            }
            if (criteria.getImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgUrl(), DocAuthor_.imgUrl));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), DocAuthor_.url));
            }
            if (criteria.getDocId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDocId(), root -> root.join(DocAuthor_.docs, JoinType.LEFT).get(Doc_.id))
                    );
            }
        }
        return specification;
    }
}
