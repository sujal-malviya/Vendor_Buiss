package com.vendorhub.vendor_onboarding.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Turns exceptions into clean JSON error responses with the right HTTP status,
 * instead of every problem becoming "500 Internal Server Error".
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleNotFound(ResourceNotFoundException ex)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // @Valid failed on a request body (POST / PUT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleInvalidBody(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return validationProblem(errors);
    }

    // Entity validation failed while saving (e.g. a PATCH that set a required field to "")
    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolation(ConstraintViolationException ex)
    {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations()
                .forEach(violation -> errors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage()));
        return validationProblem(errors);
    }

    // Same as above, but when the check happens at commit time Spring wraps it in a TransactionSystemException
    @ExceptionHandler(TransactionSystemException.class)
    ProblemDetail handleTransactionFailure(TransactionSystemException ex)
    {
        if (ex.getRootCause() instanceof ConstraintViolationException violation)
        {
            return handleConstraintViolation(violation);
        }
        throw ex;
    }

    // Duplicate unique value, or deleting a row that other rows still point to
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "This conflicts with existing data (a duplicate value, or a record that is still in use).");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail handleUnreadableBody(HttpMessageNotReadableException ex)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request body is missing or is not valid JSON.");
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ProblemDetail> handleResponseStatus(ResponseStatusException ex)
    {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getBody());
    }

    private ProblemDetail validationProblem(Map<String, String> errors)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setProperty("errors", errors);
        return problem;
    }
}
