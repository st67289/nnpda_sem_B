package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 160)
    private String title;

    @NotNull
    private TicketType type;

    private TicketPriority priority;

    private TicketState state;

    private Long solverId;

    private LocalDateTime lastModifiedAt;

    private List<TicketHistoryDTO> histories;

    private List<TicketCommentDTO> comments;

    private List<TicketAttachmentDTO> attachments;
}