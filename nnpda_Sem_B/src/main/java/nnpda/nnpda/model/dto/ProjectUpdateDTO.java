package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnpda.nnpda.model.enums.ProjectState;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProjectUpdateDTO {
    @Size(min = 1, max = 120)
    private String name;
    private String description;
    private ProjectState state;
}