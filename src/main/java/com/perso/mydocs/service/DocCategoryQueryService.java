package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.DocCategory;
import com.perso.mydocs.repository.DocCategoryRepository;
import com.perso.mydocs.service.criteria.DocCategoryCriteria;
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
 * Service for executing complex queries for {@link DocCategory} entities in the database.
 * The main input is a {@link DocCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocCategory} or a {@link Page} of {@link DocCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocCategoryQueryService extends QueryService<DocCategory> {

    private final Logger log = LoggerFactory.getLogger(DocCategoryQueryService.class);

    private final DocCategoryRepository docCategoryRepository;

    public DocCategoryQueryService(DocCategoryRepository docCategoryRepository) {
        this.docCategoryRepository = docCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link DocCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocCategory> findByCriteria(DocCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocCategory> specification = createSpecification(criteria);
        return docCategoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocCategory> findByCriteria(DocCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocCategory> specification = createSpecification(criteria);
        return docCategoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocCategory> specification = createSpecification(criteria);
        return docCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link DocCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocCategory> createSpecification(DocCategoryCriteria criteria) {
        Specification<DocCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DocCategory_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), DocCategory_.code));
            }
            if (criteria.getDocId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDocId(), root -> root.join(DocCategory_.docs, JoinType.LEFT).get(Doc_.id))
                    );
            }
        }
        return specification;
    }
}
