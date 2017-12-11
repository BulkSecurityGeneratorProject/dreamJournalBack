package pl.teneusz.dream.journal.repository;

import org.springframework.data.repository.query.Param;
import pl.teneusz.dream.journal.domain.Comment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select comment from Comment comment where comment.user.login = ?#{principal.username}")
    List<Comment> findByUserIsCurrentUser();

    @Query("select  comment from Comment comment where comment.dream.id = :id")
    List<Comment> findByDreamId(@Param("id")Long id);

}
