package pl.teneusz.dream.journal.repository;

import pl.teneusz.dream.journal.domain.Dream;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Dream entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DreamRepository extends JpaRepository<Dream, Long> {

    @Query("select dream from Dream dream where dream.user.login = ?#{principal.username}")
    List<Dream> findByUserIsCurrentUser();
    @Query("select distinct dream from Dream dream left join fetch dream.tags")
    List<Dream> findAllWithEagerRelationships();

    @Query("select dream from Dream dream left join fetch dream.tags where dream.id =:id")
    Dream findOneWithEagerRelationships(@Param("id") Long id);

}
