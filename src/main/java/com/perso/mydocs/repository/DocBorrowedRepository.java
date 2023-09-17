package com.perso.mydocs.repository;

import com.perso.mydocs.domain.DocBorrowed;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocBorrowed entity.
 */
@Repository
public interface DocBorrowedRepository extends JpaRepository<DocBorrowed, Long>, JpaSpecificationExecutor<DocBorrowed> {
    default Optional<DocBorrowed> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocBorrowed> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocBorrowed> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct docBorrowed from DocBorrowed docBorrowed left join fetch docBorrowed.doc",
        countQuery = "select count(distinct docBorrowed) from DocBorrowed docBorrowed"
    )
    Page<DocBorrowed> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct docBorrowed from DocBorrowed docBorrowed left join fetch docBorrowed.doc")
    List<DocBorrowed> findAllWithToOneRelationships();

    @Query("select docBorrowed from DocBorrowed docBorrowed left join fetch docBorrowed.doc where docBorrowed.id =:id")
    Optional<DocBorrowed> findOneWithToOneRelationships(@Param("id") Long id);
}
