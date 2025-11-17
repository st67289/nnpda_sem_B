package nnpda.nnpda.repository;

import nnpda.nnpda.model.search.TicketDocument;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Profile("!test")
public interface TicketSearchRepository extends ElasticsearchRepository<TicketDocument, Long> {
}