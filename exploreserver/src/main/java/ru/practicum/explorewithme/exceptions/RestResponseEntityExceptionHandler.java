//package ru.practicum.explorewithme.exceptions;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.ServletWebRequest;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import java.time.OffsetDateTime;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@RestControllerAdvice
//@Slf4j
//public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//    private static final String TIMESTAMP = "timestamp";
//    private static final String STATUS = "status";
//    private static final String ERROR = "error";
//    private static final String PATH = "path";
//    private static final String REASONS = "reasons";
//
//    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
//    protected ResponseEntity<Object> handleMethodMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
//        Map<String, Object> body;
//        Throwable cause = ex.getCause() // First cause is a ConversionException
//                .getCause(); // Second Cause is your custom exception or some other exception e.g. NullPointerException
//        if (Objects.equals(cause.getClass(), UnsupportedBookingStatusFilterException.class)) {
//            body = getErrorBody(HttpStatus.BAD_REQUEST, request, cause.getMessage());
//        } else {
//            body = getErrorBody(HttpStatus.BAD_REQUEST, request, ex.getMessage());
//        }
//        return ResponseEntity.badRequest().body(body);
//    }
//
//    @ExceptionHandler({NotFoundException.class, PermissionViolationException.class})
//    protected ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {
//        Map<String, Object> body = getErrorBody(HttpStatus.NOT_FOUND, request, ex.getMessage());
//        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//    }
//
//    @ExceptionHandler(value = BadRequestException.class)
//    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex, WebRequest request) {
//        Map<String, Object> body = getErrorBody(HttpStatus.BAD_REQUEST, request, ex.getMessage());
//        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }
//
//    private Map<String, Object> getErrorBody(HttpStatus status,
//                                             WebRequest request,
//                                             String error) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put(TIMESTAMP, OffsetDateTime.now());
//        body.put(STATUS, status.value());
//        body.put(REASONS, status.getReasonPhrase());
//        body.put(PATH, getRequestURI(request));
//        body.put(ERROR, error);
//        return body;
//    }
//
//    private String getRequestURI(WebRequest request) {
//        if (request instanceof ServletWebRequest) {
//            HttpServletRequest requestHttp = ((ServletWebRequest) request).getRequest();
//            return String.format("%s %s", requestHttp.getMethod(), requestHttp.getRequestURI());
//        } else {
//            return "";
//        }
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  HttpHeaders headers, HttpStatus status,
//                                                                  WebRequest request) {
//        Map<String, Object> body = getErrorBody(status, request, ex.getMessage());
//        return new ResponseEntity<>(body, headers, status);
//    }
//}
