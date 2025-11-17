package nnpda.nnpda.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDTO {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 6)
    private String newPassword;
}
