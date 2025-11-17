package nnpda.nnpda.model.dto;

import lombok.Builder;
import lombok.Data;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;

@Data
@Builder
public class TicketSearchResultDTO {
    private Long id;
    private String title;
    private TicketType type;
    private TicketPriority priority;
    private TicketState state;
    private Long projectId;
    private String resolver;
}