package nnpda.nnpda.Mapper;

import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.Ticket;
import nnpda.nnpda.model.dto.CreateTicketDTO;
import nnpda.nnpda.model.dto.TicketDTO;
import nnpda.nnpda.model.enums.TicketState;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketDTO toDto(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .type(ticket.getType())
                .priority(ticket.getPriority())
                .state(ticket.getState())
                .build();
    }

    public Ticket toEntity(TicketDTO dto, Project project) {
        return Ticket.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .priority(dto.getPriority())
                .state(dto.getState() != null ? dto.getState() : TicketState.OPEN)
                .project(project)
                .build();
    }

    public Ticket toEntity(CreateTicketDTO dto, Project project) {
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setType(dto.getType());
        ticket.setPriority(dto.getPriority());
        ticket.setState(TicketState.OPEN);
        ticket.setProject(project);
        return ticket;
    }
}
