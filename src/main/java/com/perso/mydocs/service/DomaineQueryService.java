package com.perso.mydocs.service;

import com.perso.mydocs.domain.*; // for static metamodels
import com.perso.mydocs.domain.Domaine;
import com.perso.mydocs.repository.DomaineRepository;
import com.perso.mydocs.service.criteria.DomaineCriteria;
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
 * Service for executing complex queries for {@link Domaine} entities in the database.
 * The main input is a {@link DomaineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Domaine} or a {@link Page} of {@link Domaine} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DomaineQueryService extends QueryService<Domaine> {

    private final Logger log = LoggerFactory.getLogger(DomaineQueryService.class);

    private final DomaineRepository domaineRepository;

    public DomaineQueryService(DomaineRepository domaineRepository) {
        this.domaineRepository = domaineRepository;
    }

    /**
     * Return a {@link List} of {@link Domaine} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Domaine> findByCriteria(DomaineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Domaine> specification = createSpecification(criteria);
        return domaineRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Domaine} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Domaine> findByCriteria(DomaineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Domaine> specification = createSpecification(criteria);
        return domaineRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DomaineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Domaine> specification = createSpecification(criteria);
        return domaineRepository.count(specification);
    }

    /**
     * Function to convert {@link DomaineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Domaine> createSpecification(DomaineCriteria criteria) {
        Specification<Domaine> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Domaine_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Domaine_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Domaine_.code));
            }
            if (criteria.getTopicId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTopicId(), root -> root.join(Domaine_.topics, JoinType.LEFT).get(DocTopic_.id))
                    );
            }
        }
        return specification;
    }
}
