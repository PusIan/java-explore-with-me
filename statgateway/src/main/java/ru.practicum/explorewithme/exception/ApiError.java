package ru.practicum.explorewithme.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final List<String> errors;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(HttpStatus status, String reason, String message, String error) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        if (error != null && !error.isBlank()) {
            this.errors = Collections.singletonList(error);
        } else {
            this.errors = null;
        }
    }
}