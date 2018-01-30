package pl.teneusz.dream.journal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.teneusz.dream.journal.domain.Dream;
import pl.teneusz.dream.journal.domain.DreamHelper;
import pl.teneusz.dream.journal.domain.Tag;
import pl.teneusz.dream.journal.domain.enumeration.GenderEnum;
import pl.teneusz.dream.journal.service.dto.DiagramDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Spring Data JPA repository for the Dream entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DreamRepository extends JpaRepository<Dream, Long> {

    @Query("select dream from Dream dream where dream.user.login = ?#{principal.username} order by dream.id desc")
    Page<Dream> findByUserIsCurrentUser(Pageable pageable);

    @Query("select dream from Dream dream where dream.visibility = true and dream.user is not null and " +
        "(dream.isAdult = false or" +
        "( dream.isAdult = true and (year(:birthDate) >= 18 and month(:birthDate) >= month(curent_date()) and month(:birthDate) >= month(curent_date()) ))) " +
        "order by dream.id desc")
    Page<Dream> getAllDreams(Pageable pageable, @Param("birthDate") Date birthDate);
    @Query("select dream from Dream dream join dream.tags t where dream.visibility = true and t.name = :tag and dream.user is not null order by dream.id desc")
    Page<Dream> getDreamsByTag(Pageable pageable,@Param("tag") String tag);

    @Query("select dream from Dream dream left join fetch dream.tags where dream.id =:id")
    Dream findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select new pl.teneusz.dream.journal.service.dto.DiagramDto(dream.score, count(dream.score)) from Dream dream inner join UserDetails details on details.user.id = dream.user.id where year(details.birthDate) between :down and :up group by dream.score order by dream.score")
    List<DiagramDto> getDiagramScoreByBirthDate(@Param("down") Integer down, @Param("up") Integer up);

    @Query("select new pl.teneusz.dream.journal.domain.DreamHelper(dream.id,count(elements( dream.comments))) from Dream dream where dream.id in (:ids) group by dream.id")
    List<DreamHelper> getAdditionalInfo(@Param("ids")List<Long> ids);

    @Query("select dream from Dream dream where dream.user.id = :id")
    Page<Dream> findDreamsByUserId(@Param("id")Long id, Pageable pageable);


    @Query("select dream from Dream dream where year(dream.createDate) between :down and :up")
    List<Dream> findHowManyDreamsWithTagBetweenYear(@Param("down") Integer down, @Param("up") Integer up);

    @Query("select new pl.teneusz.dream.journal.service.dto.DiagramDto(year(dream.createDate), count(dream)) " +
        "from Dream dream " +
        "where (year(dream.createDate) between :downCreate and :upCreate) " +
        "and dream.user.userDetails.gender = :gender " +
        "and (year(dream.user.userDetails.birthDate) between :downBirth and :upBirth) " +
      "group by year(dream.createDate)")
    List<DiagramDto> foo(@Param("gender") GenderEnum gender,@Param("downCreate") Integer down, @Param("upCreate") Integer up, @Param("downBirth") Integer downB, @Param("upBirth") Integer upB);
}
