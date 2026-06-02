package com.shopHMsic.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopHMsic.config.MessageTemplate;
import com.shopHMsic.exception.*;
import com.shopHMsic.ultilities.MapperUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageTemplate messageTemplate;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), ex.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<?> entityValidationException(EntityValidationException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                ex.getMessage(),
                Objects.nonNull(ex.getDetails()) ? ex.getDetails() : ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PartialUpdateException.class)
    public ResponseEntity<?> partialUpdateException(PartialUpdateException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                ex.getMessage(),
                Objects.nonNull(ex.getDetails()) ? ex.getDetails() : ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalAccessException.class, InvocationTargetException.class})
    public ResponseEntity<?> illegalArgumentException(Exception ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.validation"), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationException(AuthenticationException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.unauthorized"), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.forbidden"), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestException(BadRequestException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.badrequest"), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<?> optimisticLockingFailureException(OptimisticLockingFailureException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                messageTemplate.message("error.optimistic-locking"),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), ex.getMessage(), "", request.getDescription(false));
        Map<String, String> errors = new HashMap<>();
        Class<?> clazz = MapperUtil.getClassForObject(ex.getBindingResult().getTarget());

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = MapperUtil.convertToJsonName(clazz, ((FieldError) error).getField());
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        if (!CollectionUtils.isEmpty(errors)) {
            errorDetail.setMessage(messageTemplate.message("error.validation"));
            errorDetail.setDetail(errors);
        }

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), ex.getMessage(), "", request.getDescription(false));
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations()
                .parallelStream()
                .forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        if (!CollectionUtils.isEmpty(errors)) {
            errorDetail.setMessage(messageTemplate.message("error.validation"));
            errorDetail.setDetail(errors);
        }

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.validation"), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProcessingException.class)
    public final ResponseEntity<?> processingException(ProcessingException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.system"), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public final ResponseEntity<?> resourceAccessException(ResourceAccessException ex, WebRequest request) {
        if (ex.getCause() instanceof ResourceNotFoundException exception) {
            ErrorDetail errorDetail;

            try {
                ObjectMapper mapper = new ObjectMapper();
                errorDetail = mapper.readValue(exception.getMessage(), ErrorDetail.class);
            } catch (Exception e) {
                errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.system"), "", request.getDescription(false));
            }

            return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
        } else {
            ErrorDetail errorDetail;

            try {
                ObjectMapper mapper = new ObjectMapper();
                errorDetail = mapper.readValue(ex.getMessage(), ErrorDetail.class);
            } catch (Exception e) {
                errorDetail = new ErrorDetail(new Date(), messageTemplate.message("error.system"), ex.getMessage(), request.getDescription(false));
            }

            return new ResponseEntity<>(errorDetail, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ErrorDetail errorDetail = new ErrorDetail(new Date(), "System Error!", "System Error!", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
