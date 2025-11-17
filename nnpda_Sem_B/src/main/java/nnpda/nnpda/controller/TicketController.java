package nnpda.nnpda.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nnpda.nnpda.Mapper.TicketMapper;
import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.Ticket;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.model.dto.CreateTicketDTO;
import nnpda.nnpda.model.dto.TicketDTO;
import nnpda.nnpda.model.dto.TicketUpdateDTO;
import nnpda.nnpda.service.AuthService;
import nnpda.nnpda.service.ProjectService;
import nnpda.nnpda.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final AuthService authService;
    private final ProjectService projectService;

    private User currentUser(UserDetails principal) {
        return authService.loadUserByUsername(principal.getUsername());
    }

    @Operation(
            summary = "Vypsat všechny tickety projektu",
            description = "Vrátí seznam ticketů pro daný projekt přihlášeného uživatele."
    )
    @GetMapping
    public List<TicketDTO> getTickets(@PathVariable Long projectId,
                                      @AuthenticationPrincipal UserDetails principal) {
        return ticketService.getTicketsForProject(projectId, currentUser(principal))
                .stream()
                .map(ticketMapper::toDto)
                .toList();
    }

    @Operation(
            summary = "Vytvořit nový ticket",
            description = "Vytvoří ticket v daném projektu. Vrací 201 Created s Location hlavičkou."
    )
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@PathVariable Long projectId,
                                                  @RequestBody @Valid CreateTicketDTO createTicketDTO,
                                                  @AuthenticationPrincipal UserDetails principal) {
        Project project = projectService.getProjectByIdAndOwner(projectId, currentUser(principal));
        Ticket ticket = ticketMapper.toEntity(createTicketDTO, project);
        Ticket saved = ticketService.createTicket(projectId, ticket, currentUser(principal));
        TicketDTO body = ticketMapper.toDto(saved);

        return ResponseEntity
                .created(URI.create("/projects/" + projectId + "/tickets/" + saved.getId()))
                .body(body);
    }

    @Operation(
            summary = "Získat detail ticketu",
            description = "Vrátí ticket podle ID v rámci projektu přihlášeného uživatele."
    )
    @GetMapping("/{ticketId}")
    public TicketDTO getTicket(@PathVariable Long projectId,
                               @PathVariable Long ticketId,
                               @AuthenticationPrincipal UserDetails principal) {
        Ticket ticket = ticketService.getTicket(projectId, ticketId, currentUser(principal));
        return ticketMapper.toDto(ticket);
    }

    @Operation(
            summary = "Plná aktualizace ticketu (PUT)",
            description = "Nahradí celý ticket daty z těla požadavku."
    )
    @PutMapping("/{ticketId}")
    public TicketDTO updateTicket(@PathVariable Long projectId,
                                  @PathVariable Long ticketId,
                                  @RequestBody @Valid TicketDTO ticketDTO,
                                  @AuthenticationPrincipal UserDetails principal) {
        Project project = projectService.getProjectByIdAndOwner(projectId, currentUser(principal));
        Ticket updatedEntity = ticketMapper.toEntity(ticketDTO, project);
        Ticket updated = ticketService.updateTicket(projectId, ticketId, updatedEntity, currentUser(principal));
        return ticketMapper.toDto(updated);
    }

    @Operation(
            summary = "Částečná aktualizace ticketu (PATCH)",
            description = "Upraví pouze zadané vlastnosti ticketu (title, type, priority, state)."
    )
    @PatchMapping("/{ticketId}")
    public TicketDTO patchTicket(@PathVariable Long projectId,
                                 @PathVariable Long ticketId,
                                 @RequestBody @Valid TicketUpdateDTO ticketDto,
                                 @AuthenticationPrincipal UserDetails principal) {
        Ticket ticket = ticketService.getTicket(projectId, ticketId, currentUser(principal));

        if (ticketDto.getTitle() != null) ticket.setTitle(ticketDto.getTitle());
        if (ticketDto.getType() != null) ticket.setType(ticketDto.getType());
        if (ticketDto.getPriority() != null) ticket.setPriority(ticketDto.getPriority());
        if (ticketDto.getState() != null) ticket.setState(ticketDto.getState());

        Ticket updated = ticketService.updateTicket(projectId, ticketId, ticket, currentUser(principal));
        return ticketMapper.toDto(updated);
    }

    @Operation(
            summary = "Smazat ticket",
            description = "Smaže ticket v daném projektu. Vrací 204 No Content."
    )
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long projectId,
                                             @PathVariable Long ticketId,
                                             @AuthenticationPrincipal UserDetails principal) {
        ticketService.deleteTicket(projectId, ticketId, currentUser(principal));
        return ResponseEntity.noContent().build();
    }
}
