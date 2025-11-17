package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketType;
@Data
public class CreateTicketDTO {
    @NotBlank
    @Size(min = 1, max = 160)
    private String title;

    @NotNull
    private TicketType type;
    @NotNull
    private TicketPriority priority;
}