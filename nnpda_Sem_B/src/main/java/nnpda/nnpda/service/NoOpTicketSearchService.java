package nnpda.nnpda.service;

import nnpda.nnpda.model.Entity.Ticket;
import nnpda.nnpda.model.dto.TicketSearchResultDTO;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Profile("test")
public class NoOpTicketSearchService implements TicketSearchFacade {
    @Override
    public void indexTicket(Ticket ticket) {
        // no-op for tests
    }

    @Override
    public void deleteTicket(Long ticketId) {
        // no-op for tests
    }

    @Override
    public Page<TicketSearchResultDTO> searchTickets(String text, TicketType type, TicketPriority priority, TicketState state, Long projectId, Long ownerId, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
}