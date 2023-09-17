package com.perso.mydocs.repository;

import com.perso.mydocs.domain.Domaine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Domaine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomaineRepository extends JpaRepository<Domaine, Long>, JpaSpecificationExecutor<Domaine> {}
