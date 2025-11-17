package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import nnpda.nnpda.model.enums.ProjectState;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 120)
    private String name;

    private String description;

    @NotNull
    private ProjectState state;
}