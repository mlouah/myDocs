package com.perso.mydocs.repository;

import com.perso.mydocs.domain.DocPublisher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocPublisher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocPublisherRepository extends JpaRepository<DocPublisher, Long>, JpaSpecificationExecutor<DocPublisher> {}
