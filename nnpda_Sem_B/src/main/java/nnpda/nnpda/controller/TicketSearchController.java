package nnpda.nnpda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nnpda.nnpda.model.dto.TicketSearchResultDTO;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;
import nnpda.nnpda.service.AuthService;
import nnpda.nnpda.service.TicketSearchFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets/search")
@RequiredArgsConstructor
@Tag(name = "Ticket Search")
public class TicketSearchController {

    private final TicketSearchFacade ticketSearchService;
    private final AuthService authService;

    @Operation(summary = "Fulltext vyhledávání ticketů", description = "Podporuje fulltext, filtrování, stránkování a řazení.")
    @GetMapping
    public Page<TicketSearchResultDTO> search(
            @RequestParam(value = "q", required = false) String text,
            @RequestParam(required = false) TicketType type,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) TicketState state,
            @RequestParam(required = false) Long projectId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails principal
    ) {
        Long ownerId = authService.loadUserByUsername(principal.getUsername()).getId();
        return ticketSearchService.searchTickets(text, type, priority, state, projectId, ownerId, pageable);
    }
}