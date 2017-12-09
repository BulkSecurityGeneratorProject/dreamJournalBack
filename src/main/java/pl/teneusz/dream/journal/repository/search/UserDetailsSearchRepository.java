package pl.teneusz.dream.journal.repository.search;

import pl.teneusz.dream.journal.domain.UserDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserDetails entity.
 */
public interface UserDetailsSearchRepository extends ElasticsearchRepository<UserDetails, Long> {
}
