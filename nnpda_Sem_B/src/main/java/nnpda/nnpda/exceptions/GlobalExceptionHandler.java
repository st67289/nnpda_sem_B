package nnpda.nnpda.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatField)
                .collect(Collectors.joining("; "));
        return body(400, "Bad Request", "VALIDATION_FAILED", msg, req.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleUnique(DataIntegrityViolationException ex, HttpServletRequest req) {
        return body(409, "Conflict", "CONSTRAINT_VIOLATION", "Unique constraint violated", req.getRequestURI());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handle409(ConflictException ex, HttpServletRequest req) {
        return body(409, "Conflict", "CONFLICT", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handle403(ForbiddenException ex, HttpServletRequest req) {
        return body(403, "Forbidden", "FORBIDDEN", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle404(NotFoundException ex, HttpServletRequest req) {
        return body(404, "Not Found", "NOT_FOUND", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> handle422(UnprocessableEntityException ex, HttpServletRequest req) {
        return body(422, "Unprocessable Entity", "WORKFLOW_ERROR", ex.getMessage(), req.getRequestURI());
    }

    private String formatField(FieldError e) {
        return e.getField() + ": " + e.getDefaultMessage();
    }

    private Map<String, Object> body(int status, String error, String code, String msg, String path) {
        return Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", status,
                "error", error,
                "code", code,
                "message", msg,
                "path", path
        );
    }
}
