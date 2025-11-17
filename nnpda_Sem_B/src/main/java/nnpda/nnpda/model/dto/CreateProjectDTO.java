package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectDTO {
    @NotBlank
    @Size(min = 1, max = 120)
    private String name;
    private String description;
}
