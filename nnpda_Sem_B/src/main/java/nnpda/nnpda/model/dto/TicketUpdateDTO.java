package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateDTO {

    @Size(min = 1, max = 160)
    private String title;

    private TicketType type;

    private TicketPriority priority;

    private TicketState state;

    private Long solverId;
}
