package com.perso.mydocs.repository;

import com.perso.mydocs.domain.DocCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocCategoryRepository extends JpaRepository<DocCategory, Long>, JpaSpecificationExecutor<DocCategory> {}
