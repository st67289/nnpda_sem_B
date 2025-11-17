package nnpda.nnpda.service;

import nnpda.nnpda.model.Entity.Ticket;
import nnpda.nnpda.model.dto.TicketSearchResultDTO;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketSearchFacade {
    void indexTicket(Ticket ticket);

    void deleteTicket(Long ticketId);

    Page<TicketSearchResultDTO> searchTickets(String text,
                                              TicketType type,
                                              TicketPriority priority,
                                              TicketState state,
                                              Long projectId,
                                              Long ownerId,
                                              Pageable pageable);
}