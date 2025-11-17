package nnpda.nnpda.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import nnpda.nnpda.model.Entity.Ticket;
import nnpda.nnpda.model.dto.TicketSearchResultDTO;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;
import nnpda.nnpda.model.search.TicketDocument;
import nnpda.nnpda.repository.TicketSearchRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
@RequiredArgsConstructor
public class TicketSearchService implements TicketSearchFacade {

    private final TicketSearchRepository ticketSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public void indexTicket(Ticket ticket) {
        ticketSearchRepository.save(toDocument(ticket));
    }

    public void deleteTicket(Long ticketId) {
        ticketSearchRepository.deleteById(ticketId);
    }

    public Page<TicketSearchResultDTO> searchTickets(
            String text,
            TicketType type,
            TicketPriority priority,
            TicketState state,
            Long projectId,
            Long ownerId,
            Pageable pageable
    ) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        boolean hasConditions = false;

        if (text != null && !text.isBlank()) {
            boolQuery.must(m -> m.simpleQueryString(s -> s.query(text).fields("title")));
            hasConditions = true;
        }

        if (type != null) {
            boolQuery.filter(f -> f.term(t -> t.field("type").value(type.name())));
            hasConditions = true;
        }
        if (priority != null) {
            boolQuery.filter(f -> f.term(t -> t.field("priority").value(priority.name())));
            hasConditions = true;
        }
        if (state != null) {
            boolQuery.filter(f -> f.term(t -> t.field("state").value(state.name())));
            hasConditions = true;
        }
        if (projectId != null) {
            boolQuery.filter(f -> f.term(t -> t.field("projectId").value(projectId)));
            hasConditions = true;
        }
        if (ownerId != null) {
            boolQuery.filter(f -> f.term(t -> t.field("ownerId").value(ownerId)));
            hasConditions = true;
        }

        if (!hasConditions) {
            boolQuery.must(m -> m.matchAll(ma -> ma));
        }

        Query query = new Query.Builder().bool(boolQuery.build()).build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(pageable)
                .build();

        SearchHits<TicketDocument> hits = elasticsearchOperations.search(nativeQuery, TicketDocument.class);
        return SearchHitSupport.searchPageFor(hits, pageable).map(this::toDto);
    }

    private TicketSearchResultDTO toDto(SearchHit<TicketDocument> hit) {
        TicketDocument doc = hit.getContent();
        return TicketSearchResultDTO.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .type(doc.getType())
                .priority(doc.getPriority())
                .state(doc.getState())
                .projectId(doc.getProjectId())
                .resolver(doc.getResolver())
                .build();
    }

    private TicketDocument toDocument(Ticket ticket) {
        return TicketDocument.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .type(ticket.getType())
                .priority(ticket.getPriority())
                .state(ticket.getState())
                .projectId(ticket.getProject() != null ? ticket.getProject().getId() : null)
                .ownerId(ticket.getProject() != null && ticket.getProject().getOwner() != null
                        ? ticket.getProject().getOwner().getId()
                        : null)
                .resolver(ticket.getProject() != null && ticket.getProject().getOwner() != null
                        ? ticket.getProject().getOwner().getUsername()
                        : null)
                .build();
    }
}