package com.perso.mydocs.repository;

import com.perso.mydocs.domain.Doc;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doc entity.
 */
@Repository
public interface DocRepository extends JpaRepository<Doc, Long>, JpaSpecificationExecutor<Doc> {
    default Optional<Doc> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Doc> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Doc> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct doc from Doc doc left join fetch doc.publisher left join fetch doc.format left join fetch doc.langue left join fetch doc.maintopic left join fetch doc.mainAuthor left join fetch doc.collection",
        countQuery = "select count(distinct doc) from Doc doc"
    )
    Page<Doc> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct doc from Doc doc left join fetch doc.publisher left join fetch doc.format left join fetch doc.langue left join fetch doc.maintopic left join fetch doc.mainAuthor left join fetch doc.collection"
    )
    List<Doc> findAllWithToOneRelationships();

    @Query(
        "select doc from Doc doc left join fetch doc.publisher left join fetch doc.format left join fetch doc.langue left join fetch doc.maintopic left join fetch doc.mainAuthor left join fetch doc.collection where doc.id =:id"
    )
    Optional<Doc> findOneWithToOneRelationships(@Param("id") Long id);
}
