package nnpda.nnpda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnpda.nnpda.model.enums.TicketState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistoryDTO {
    private Long id;
    private TicketState oldState;
    private TicketState newState;
    private Long changedById;
    private LocalDateTime changedAt;
}