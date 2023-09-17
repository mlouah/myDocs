package com.perso.mydocs.repository;

import com.perso.mydocs.domain.DocFormat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocFormat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocFormatRepository extends JpaRepository<DocFormat, Long>, JpaSpecificationExecutor<DocFormat> {}
