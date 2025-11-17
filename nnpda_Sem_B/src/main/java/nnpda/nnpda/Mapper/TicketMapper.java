package nnpda.nnpda.Mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.Ticket;
import nnpda.nnpda.model.Entity.TicketAttachment;
import nnpda.nnpda.model.Entity.TicketComment;
import nnpda.nnpda.model.Entity.TicketHistory;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.model.dto.*;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final UserRepository userRepository;

    public TicketDTO toDto(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .type(ticket.getType())
                .priority(ticket.getPriority())
                .state(ticket.getState())
                .solverId(ticket.getSolver() != null ? ticket.getSolver().getId() : null)
                .lastModifiedAt(ticket.getLastModifiedAt())
                .histories(mapHistories(ticket.getHistories()))
                .comments(mapComments(ticket.getComments()))
                .attachments(mapAttachments(ticket.getAttachments()))
                .build();
    }

    public Ticket toEntity(TicketDTO dto, Project project) {
        return Ticket.builder()
                .title(dto.getTitle())
                .type(dto.getType())
                .priority(dto.getPriority())
                .state(dto.getState() != null ? dto.getState() : TicketState.OPEN)
                .solver(resolveUser(dto.getSolverId()))
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
        ticket.setSolver(resolveUser(dto.getSolverId()));
        return ticket;
    }

    private List<TicketHistoryDTO> mapHistories(List<TicketHistory> histories) {
        if (histories == null) return Collections.emptyList();
        return histories.stream()
                .filter(Objects::nonNull)
                .map(history -> TicketHistoryDTO.builder()
                        .id(history.getId())
                        .oldState(history.getOldState())
                        .newState(history.getNewState())
                        .changedById(history.getChangedBy() != null ? history.getChangedBy().getId() : null)
                        .changedAt(history.getChangedAt())
                        .build())
                .toList();
    }

    private List<TicketCommentDTO> mapComments(List<TicketComment> comments) {
        if (comments == null) return Collections.emptyList();
        return comments.stream()
                .filter(Objects::nonNull)
                .map(comment -> TicketCommentDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .authorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null)
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();
    }

    private List<TicketAttachmentDTO> mapAttachments(List<TicketAttachment> attachments) {
        if (attachments == null) return Collections.emptyList();
        return attachments.stream()
                .filter(Objects::nonNull)
                .map(attachment -> TicketAttachmentDTO.builder()
                        .id(attachment.getId())
                        .fileName(attachment.getFileName())
                        .url(attachment.getUrl())
                        .uploadedById(attachment.getUploadedBy() != null ? attachment.getUploadedBy().getId() : null)
                        .uploadedAt(attachment.getUploadedAt())
                        .build())
                .toList();
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for solverId: " + userId));
    }
}