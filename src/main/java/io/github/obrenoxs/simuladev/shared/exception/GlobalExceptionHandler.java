package io.github.obrenoxs.simuladev.shared.exception;

import io.github.obrenoxs.simuladev.ai.exception.AiResponseException;
import io.github.obrenoxs.simuladev.engine.exception.NoEligibleConceptsException;
import io.github.obrenoxs.simuladev.user.exception.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos", null, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> resourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null, request);
    }

    @ExceptionHandler(NoEligibleConceptsException.class)
    public ResponseEntity<Map<String, Object>> noEligibleConcepts(NoEligibleConceptsException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null, request);
    }

    @ExceptionHandler(AiResponseException.class)
    public ResponseEntity<Map<String, Object>> aiResponse(AiResponseException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_GATEWAY, ex.getMessage(), null, request);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String message, List<Map<String, String>> errors, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        if (errors != null && !errors.isEmpty()) {
            body.put("errors", errors);
        }

        body.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(status).body(body);
    }
}