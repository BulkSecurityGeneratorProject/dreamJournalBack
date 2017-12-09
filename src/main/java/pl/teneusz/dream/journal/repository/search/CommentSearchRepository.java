package pl.teneusz.dream.journal.repository.search;

import pl.teneusz.dream.journal.domain.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Comment entity.
 */
public interface CommentSearchRepository extends ElasticsearchRepository<Comment, Long> {
}
