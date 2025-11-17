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
public class TicketCommentDTO {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String content;

    private Long authorId;
    private LocalDateTime createdAt;
}