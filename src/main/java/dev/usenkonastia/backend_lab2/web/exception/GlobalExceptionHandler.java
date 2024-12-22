package dev.usenkonastia.backend_lab2.web.exception;

import dev.usenkonastia.backend_lab2.service.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

import static dev.usenkonastia.backend_lab2.web.exception.ProblemDetailsUtils.getValidationErrorsProblemDetail;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("user-not-found"));
        problemDetail.setTitle("User Not Found");
        return problemDetail;
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    ProblemDetail handleCategoryNotFound(CategoryNotFoundException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("category-not-found"));
        problemDetail.setTitle("Category Not Found");
        return problemDetail;
    }

    @ExceptionHandler(RecordNotFoundException.class)
    ProblemDetail handleRecordNotFound(RecordNotFoundException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("record-not-found"));
        problemDetail.setTitle("Record Not Found");
        return problemDetail;
    }

    @ExceptionHandler(InvalidArgumentsException.class)
    ProblemDetail handleInvalidArguments(InvalidArgumentsException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("invalid-arguments"));
        problemDetail.setTitle("Invalid Arguments");
        return problemDetail;
    }

    @ExceptionHandler(PersistenceException.class)
    ProblemDetail handlePersistenceException(PersistenceException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setType(URI.create("persistence-exception"));
        problemDetail.setTitle("Persistence exception");
        return problemDetail;
    }

    @ExceptionHandler(ForbiddenException.class)
    ProblemDetail handleForbiddenException(ForbiddenException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(FORBIDDEN, ex.getMessage());
        problemDetail.setType(URI.create("forbidden-exception"));
        problemDetail.setTitle("Forbidden exception");
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<ParamsViolationDetails> validationResponse =
                errors.stream().map(err -> ParamsViolationDetails.builder().reason(err.getDefaultMessage()).fieldName(err.getField()).build()).toList();
        return ResponseEntity.status(BAD_REQUEST).body(getValidationErrorsProblemDetail(validationResponse));
    }

    @ExceptionHandler(AuthenticationException.class)
    ProblemDetail handleAuthenticationException(AuthenticationException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(UNAUTHORIZED, ex.getMessage());
        problemDetail.setType(URI.create("authentication-exception"));
        problemDetail.setTitle("Authentication exception");
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
        ProblemDetail problemDetail = forStatusAndDetail(UNAUTHORIZED, ex.getMessage());
        problemDetail.setType(URI.create("bad-credentials-exception"));
        problemDetail.setTitle("Bad credentials exception");
        return problemDetail;
    }
}
