package com.perso.mydocs.repository;

import com.perso.mydocs.domain.DocTopic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocTopic entity.
 */
@Repository
public interface DocTopicRepository extends JpaRepository<DocTopic, Long>, JpaSpecificationExecutor<DocTopic> {
    default Optional<DocTopic> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocTopic> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocTopic> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct docTopic from DocTopic docTopic left join fetch docTopic.domaine",
        countQuery = "select count(distinct docTopic) from DocTopic docTopic"
    )
    Page<DocTopic> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct docTopic from DocTopic docTopic left join fetch docTopic.domaine")
    List<DocTopic> findAllWithToOneRelationships();

    @Query("select docTopic from DocTopic docTopic left join fetch docTopic.domaine where docTopic.id =:id")
    Optional<DocTopic> findOneWithToOneRelationships(@Param("id") Long id);
}
