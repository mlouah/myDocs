package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.DocFormat;
import com.perso.mydocs.repository.DocFormatRepository;
import com.perso.mydocs.service.criteria.DocFormatCriteria;
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
 * Service for executing complex queries for {@link DocFormat} entities in the database.
 * The main input is a {@link DocFormatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocFormat} or a {@link Page} of {@link DocFormat} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocFormatQueryService extends QueryService<DocFormat> {

    private final Logger log = LoggerFactory.getLogger(DocFormatQueryService.class);

    private final DocFormatRepository docFormatRepository;

    public DocFormatQueryService(DocFormatRepository docFormatRepository) {
        this.docFormatRepository = docFormatRepository;
    }

    /**
     * Return a {@link List} of {@link DocFormat} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocFormat> findByCriteria(DocFormatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocFormat> specification = createSpecification(criteria);
        return docFormatRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocFormat} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocFormat> findByCriteria(DocFormatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocFormat> specification = createSpecification(criteria);
        return docFormatRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocFormatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocFormat> specification = createSpecification(criteria);
        return docFormatRepository.count(specification);
    }

    /**
     * Function to convert {@link DocFormatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocFormat> createSpecification(DocFormatCriteria criteria) {
        Specification<DocFormat> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocFormat_.id));
            }
            if (criteria.getFormat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFormat(), DocFormat_.format));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), DocFormat_.code));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), DocFormat_.notes));
            }
            if (criteria.getDocId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDocId(), root -> root.join(DocFormat_.docs, JoinType.LEFT).get(Doc_.id))
                    );
            }
        }
        return specification;
    }
}
