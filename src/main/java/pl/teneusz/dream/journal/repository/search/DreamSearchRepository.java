package pl.teneusz.dream.journal.repository.search;

import pl.teneusz.dream.journal.domain.Dream;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Dream entity.
 */
public interface DreamSearchRepository extends ElasticsearchRepository<Dream, Long> {
}
