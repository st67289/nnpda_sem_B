package nnpda.nnpda.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nnpda.nnpda.exceptions.ForbiddenException;
import nnpda.nnpda.exceptions.NotFoundException;
import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.Ticket;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.service.TicketSearchFacade;
import nnpda.nnpda.repository.ProjectRepository;
import nnpda.nnpda.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final TicketSearchFacade ticketSearchService;

    public List<Ticket> getTicketsForProject(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        if (!project.getOwner().equals(user)) {
            throw new ForbiddenException("Access to foreign project is forbidden");
        }

        return ticketRepository.findByProject(project);
    }

    public Ticket createTicket(Long projectId, Ticket ticket, User user) {
        Project project = projectRepository.findByIdAndOwner(projectId, user)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        ticket.setProject(project);
        Ticket saved = ticketRepository.save(ticket);
        ticketSearchService.indexTicket(saved);
        return saved;
    }

    public Ticket getTicket(Long projectId, Long ticketId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (!project.getOwner().equals(user)) {
            throw new ForbiddenException("Access to foreign project is forbidden");
        }

        return ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }

    public Ticket updateTicket(Long projectId, Long ticketId, Ticket updatedTicket, User user) {
        Ticket ticket = getTicket(projectId, ticketId, user);
        ticket.setTitle(updatedTicket.getTitle());
        ticket.setType(updatedTicket.getType());
        ticket.setPriority(updatedTicket.getPriority());
        ticket.setState(updatedTicket.getState());
        Ticket saved = ticketRepository.save(ticket);
        ticketSearchService.indexTicket(saved);
        return saved;
    }
    public void deleteTicket(Long projectId, Long ticketId, User currentUser) {
        Ticket ticket = getTicket(projectId, ticketId, currentUser);
        ticketRepository.delete(ticket);
        ticketSearchService.deleteTicket(ticketId);
    }
}