package nnpda.nnpda.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.model.dto.*;
import nnpda.nnpda.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Registrace nového uživatele",
            description = "Vytvoří nového uživatele a vrátí jeho základní profil (id, username, email)."
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@RequestBody @Valid RegisterDTO dto) {
        User user = authService.register(dto);
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Operation(
            summary = "Přihlášení a získání JWT",
            description = "Přihlášení pomocí username/email a hesla. V odpovědi vrací JWT token."
    )
    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Valid LoginDTO dto) {
        String jwt = authService.login(dto);
        return new TokenDTO(jwt);
    }

    @Operation(
            summary = "Vyžádání resetovacího kódu hesla",
            description = "Odešle uživateli resetovací kód (např. e-mailem) pro změnu hesla."
    )
    @PostMapping("/request-password-reset")
    public MessageDTO requestPasswordReset(@RequestBody @Valid PasswordResetRequestDTO dto) {
        String msg = authService.requestPasswordReset(dto);
        return new MessageDTO(msg);
    }

    @Operation(
            summary = "Reset hesla pomocí kódu",
            description = "Změní heslo pomocí platného resetovacího kódu. Vrací 204 No Content."
    )
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody @Valid PasswordResetDTO dto) {
        authService.resetPassword(dto);
    }

    @Operation(
            summary = "Změna hesla (vyžaduje JWT)",
            description = "Změní heslo přihlášenému uživateli. Vyžaduje platný Bearer token."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid PasswordChangeDTO dto,
                               @AuthenticationPrincipal UserDetails principal) {
        User currentUser = authService.loadUserByUsername(principal.getUsername());
        authService.changePassword(currentUser, dto);
    }

    // pomocné DTO
    public record TokenDTO(String token) {}
    public record MessageDTO(String message) {}
}
