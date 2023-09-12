package ru.practicum.explorewithme.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final boolean isDetailsNeeded;

    public ErrorHandler(@Value("${api-errors.add-stack-traces: false}") boolean isDetailsNeeded) {
        this.isDetailsNeeded = isDetailsNeeded;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("Field: %s. Error: %s. Value: %s",
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                String.format("During [%s] validation %d errors were found", ex.getObjectName(), ex.getErrorCount()),
                errors
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                ex.getMessage(),
                ex.getLocalizedMessage()
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(RuntimeException ex) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "One of the restrictions has been violated",
                ex.getLocalizedMessage(), ex.getLocalizedMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                "The required object was not found.",
                ex.getLocalizedMessage(),
                error(ex)
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(value = {ConflictException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                "Wrong data provided.",
                ex.getLocalizedMessage(),
                error(ex)
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "The passed data causes an error.",
                ex.getLocalizedMessage(),
                error(ex)
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler
    public ResponseEntity<ApiError> handleAll(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error occurred",
                ex.getLocalizedMessage(),
                error(ex)
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    private String error(Exception e) {
        if (isDetailsNeeded) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String err = sw.toString();
            log.error(err);
            return err;
        } else {
            return null;
        }
    }
}
