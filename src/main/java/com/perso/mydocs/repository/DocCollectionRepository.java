package com.perso.mydocs.repository;

import com.perso.mydocs.domain.DocCollection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocCollection entity.
 */
@Repository
public interface DocCollectionRepository extends JpaRepository<DocCollection, Long>, JpaSpecificationExecutor<DocCollection> {
    default Optional<DocCollection> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocCollection> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocCollection> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct docCollection from DocCollection docCollection left join fetch docCollection.docPublisher",
        countQuery = "select count(distinct docCollection) from DocCollection docCollection"
    )
    Page<DocCollection> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct docCollection from DocCollection docCollection left join fetch docCollection.docPublisher")
    List<DocCollection> findAllWithToOneRelationships();

    @Query("select docCollection from DocCollection docCollection left join fetch docCollection.docPublisher where docCollection.id =:id")
    Optional<DocCollection> findOneWithToOneRelationships(@Param("id") Long id);
}
