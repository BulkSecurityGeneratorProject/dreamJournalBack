package pl.teneusz.dream.journal.repository;

import org.springframework.data.repository.query.Param;
import pl.teneusz.dream.journal.domain.Tag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import pl.teneusz.dream.journal.service.dto.DiagramDto;

import java.util.List;


/**
 * Spring Data JPA repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("Select new pl.teneusz.dream.journal.service.dto.DiagramDto(t.name, count(t.dreams)) from Tag t left join t.dreams d where year(d.createDate) between :from and :to group by t.name")
    List<DiagramDto> findHowManyDreamsWithTagBetweenYear(@Param("from")Long from, @Param("to") Long to);
}
