package com.publicis.sapient.cardprocessing.exception;

import com.publicis.sapient.cardprocessing.dto.ErrorDetails;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.publicis.sapient.cardprocessing.config.ServerConstants.CARD_NUMBER_UNIQUE_CONSTRAINTS;
import static com.publicis.sapient.cardprocessing.config.ServerConstants.CARD_NUMBER_UNIQUE_CONSTRAINTS_ERR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleEntityNotFound(final EntityNotFoundException ex, final WebRequest request) {
        final ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({javax.validation.ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final javax.validation.ConstraintViolationException ex) {
        final ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                "Constraint violation",
                ex.getConstraintViolations()
                        .stream()
                        .map(constraintViolation ->
                                constraintViolation.getPropertyPath() + " - " +
                                        constraintViolation)
                        .collect(Collectors.joining()));
        return buildResponseEntity(errorDetails, BAD_REQUEST);
    }

    @ExceptionHandler({org.springframework.dao.DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolation(final org.springframework.dao.DataIntegrityViolationException ex) {
        ErrorDetails errorDetails = null;
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            final org.hibernate.exception.ConstraintViolationException constraintViolationException = (org.hibernate.exception.ConstraintViolationException) ex.getCause();
            errorDetails = new ErrorDetails(LocalDateTime.now(),
                    "Data Integrity violation",
                    getErrorMessage(constraintViolationException));
        }
        return buildResponseEntity(errorDetails, BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final List<ObjectError> fieldError = ex.getBindingResult().getAllErrors();
        final List<ErrorDetails> listOfErrors = new ArrayList<>();
        fieldError.forEach(error -> {
            listOfErrors.add(ErrorDetails.builder().timestamp(LocalDateTime.now()).message(getFieldName(error) + ": is incorrect in the request")
                    .details(getDefaultMessage(error)).build());
        });
        return buildResponseEntity(listOfErrors, BAD_REQUEST);
    }

    private String getErrorMessage(final org.hibernate.exception.ConstraintViolationException ce) {
        if (ce.getConstraintName().contains(CARD_NUMBER_UNIQUE_CONSTRAINTS)) {
            return CARD_NUMBER_UNIQUE_CONSTRAINTS_ERR_MESSAGE;
        }
        return ce.getMessage();
    }

    private ResponseEntity<Object> buildResponseEntity(final ErrorDetails error, final HttpStatus status) {
        return new ResponseEntity<>(error, status);
    }

    private ResponseEntity<Object> buildResponseEntity(final List<ErrorDetails> error, final HttpStatus status) {
        return new ResponseEntity<>(error, status);
    }

    private String getFieldName(final ObjectError objectError) {
        return (objectError instanceof FieldError) ? ((FieldError) objectError).getField() : null;
    }

    private String getDefaultMessage(final ObjectError objectError) {
        return (objectError instanceof FieldError) ? ((FieldError) objectError).getDefaultMessage() : null;
    }

}
