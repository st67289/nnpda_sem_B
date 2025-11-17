package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketAttachmentDTO {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String fileName;

    @NotBlank
    @Size(min = 1, max = 500)
    private String url;

    private Long uploadedById;
    private LocalDateTime uploadedAt;
}