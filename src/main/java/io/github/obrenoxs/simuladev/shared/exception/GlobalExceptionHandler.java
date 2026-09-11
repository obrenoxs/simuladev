package io.github.obrenoxs.simuladev.shared.exception;

import io.github.obrenoxs.simuladev.user.exception.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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