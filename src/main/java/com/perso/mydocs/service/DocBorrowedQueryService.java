package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.DocBorrowed;
import com.perso.mydocs.repository.DocBorrowedRepository;
import com.perso.mydocs.service.criteria.DocBorrowedCriteria;
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
 * Service for executing complex queries for {@link DocBorrowed} entities in the database.
 * The main input is a {@link DocBorrowedCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocBorrowed} or a {@link Page} of {@link DocBorrowed} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocBorrowedQueryService extends QueryService<DocBorrowed> {

    private final Logger log = LoggerFactory.getLogger(DocBorrowedQueryService.class);

    private final DocBorrowedRepository docBorrowedRepository;

    public DocBorrowedQueryService(DocBorrowedRepository docBorrowedRepository) {
        this.docBorrowedRepository = docBorrowedRepository;
    }

    /**
     * Return a {@link List} of {@link DocBorrowed} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocBorrowed> findByCriteria(DocBorrowedCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DocBorrowed> specification = createSpecification(criteria);
        return docBorrowedRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DocBorrowed} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocBorrowed> findByCriteria(DocBorrowedCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DocBorrowed> specification = createSpecification(criteria);
        return docBorrowedRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocBorrowedCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DocBorrowed> specification = createSpecification(criteria);
        return docBorrowedRepository.count(specification);
    }

    /**
     * Function to convert {@link DocBorrowedCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DocBorrowed> createSpecification(DocBorrowedCriteria criteria) {
        Specification<DocBorrowed> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DocBorrowed_.id));
            }
            if (criteria.getBorrowDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBorrowDate(), DocBorrowed_.borrowDate));
            }
            if (criteria.getBorrowerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBorrowerName(), DocBorrowed_.borrowerName));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), DocBorrowed_.notes));
            }
            if (criteria.getDocId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDocId(), root -> root.join(DocBorrowed_.doc, JoinType.LEFT).get(Doc_.id))
                    );
            }
        }
        return specification;
    }
}
