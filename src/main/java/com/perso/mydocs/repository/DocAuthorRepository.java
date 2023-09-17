package com.perso.mydocs.repository;

import com.perso.mydocs.domain.DocAuthor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocAuthor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocAuthorRepository extends JpaRepository<DocAuthor, Long>, JpaSpecificationExecutor<DocAuthor> {}
