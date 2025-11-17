package nnpda.nnpda.service;

import lombok.RequiredArgsConstructor;
import nnpda.nnpda.exceptions.*;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.model.dto.LoginDTO;
import nnpda.nnpda.model.dto.PasswordChangeDTO;
import nnpda.nnpda.model.dto.PasswordResetDTO;
import nnpda.nnpda.model.dto.PasswordResetRequestDTO;
import nnpda.nnpda.model.dto.RegisterDTO;
import nnpda.nnpda.repository.UserRepository;
import nnpda.nnpda.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //expire 10m
    private static final long RESET_CODE_EXPIRE_MS = 10 * 60 * 1000L;

    //thread-safe reset codes: username -> token
    private final Map<String, PasswordResetToken> passwordResetTokens = new ConcurrentHashMap<>();

    private static record PasswordResetToken(String code, String username, long expiresAt) {
        boolean isExpired() { return System.currentTimeMillis() > expiresAt; }
    }

    public User register(RegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new DuplicateUserException("Username already exists");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateUserException("Email already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        return userRepository.save(user);
    }

    public String login(LoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
        return jwtUtil.generateToken(userDetails);
    }

    //generate reset code
    public String requestPasswordReset(PasswordResetRequestDTO dto) {
        User user = userRepository.findByUsername(dto.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(dto.getUsernameOrEmail()))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        long expiresAt = System.currentTimeMillis() + RESET_CODE_EXPIRE_MS;

        passwordResetTokens.put(user.getUsername(), new PasswordResetToken(code, user.getUsername(), expiresAt));
        return code;
    }


    public void resetPassword(PasswordResetDTO dto) {

        //find token
        PasswordResetToken token = passwordResetTokens.values().stream()
                .filter(t -> t.code().equals(dto.getCode()))
                .findFirst()
                .orElseThrow(() -> new InvalidResetCodeException("Invalid or expired code"));

        if (token.isExpired()) {
            passwordResetTokens.remove(token.username());
            throw new ExpiredResetCodeException("Code expired");
        }

        User user = userRepository.findByUsername(token.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        passwordResetTokens.remove(token.username());
    }

    public void changePassword(User user, PasswordChangeDTO dto) {
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
