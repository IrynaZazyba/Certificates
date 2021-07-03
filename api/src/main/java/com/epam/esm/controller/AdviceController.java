package com.epam.esm.controller;

import com.epam.esm.exception.DefaultExceptionInfo;
import com.epam.esm.exception.InvalidUserDataException;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@RequiredArgsConstructor
public class AdviceController extends ResponseEntityExceptionHandler {

    private static final String RESOURCE_NOT_FOUND = "resource.notfound";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "server.error";
    private static final String RESOURCE_ALREADY_EXISTS = "resource.already.exists";

    private final ResourceBundleMessageSource messageSource;

    /**
     * Handle exception from database that generated if
     * resource not found
     *
     * @param ex exception details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> tagHandler(ResourceNotFoundException ex) {
        String errorCode = String.valueOf(NOT_FOUND.value()) + ex.getId();
        DefaultExceptionInfo info = new DefaultExceptionInfo();
        info.setErrorMessage(messageSource
                .getMessage(RESOURCE_NOT_FOUND, new Object[]{ex.getId()}, LocaleContextHolder.getLocale()));
        info.setErrorCode(errorCode);
        return new ResponseEntity<>(
                info, new HttpHeaders(), NOT_FOUND);
    }

    /**
     * Handle exception if created resource already exists
     *
     * @param ex exception details
     */
    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseBody
    public ResponseEntity<Object> alreadyExistsHandler(ResourceAlreadyExistException ex) {
        String errorCode = String.valueOf(CONFLICT.value()) + ex.getId();
        DefaultExceptionInfo info = new DefaultExceptionInfo();
        info.setErrorMessage(messageSource
                .getMessage(RESOURCE_ALREADY_EXISTS, new Object[]{ex.getId()}, LocaleContextHolder.getLocale()));
        info.setErrorCode(errorCode);
        return new ResponseEntity<>(
                info, new HttpHeaders(), NOT_FOUND);
    }

    /**
     * Handle exception from database
     *
     * @param ex exception details
     */
    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseBody
    public ResponseEntity<Object> runtimeExceptionHandler(InvalidUserDataException ex) {
        StringBuilder errorFields = new StringBuilder();
        ex.getResult().getFieldErrors().forEach(e ->
                errorFields.append(" ")
                        .append(messageSource.getMessage(e.getCode(), null, LocaleContextHolder.getLocale()))
                        .append(","));
        errorFields.deleteCharAt(errorFields.length() - 1);
        String errorCode = String.valueOf(BAD_REQUEST.value() + ex.getResult().getErrorCount());
        DefaultExceptionInfo info = new DefaultExceptionInfo();
        info.setErrorMessage(errorFields.toString());
        info.setErrorCode(errorCode);
        return new ResponseEntity<>(
                info, new HttpHeaders(), BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(
                errors, new HttpHeaders(), BAD_REQUEST);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream().collect(Collectors.toMap(
                v -> v.getPropertyPath().toString(),
                ConstraintViolation::getMessage));
        return ResponseEntity.status(BAD_REQUEST).body(errors);
    }

    /**
     * Handle server exception
     *
     * @param ex exception details
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Object> runtimeExceptionHandler(RuntimeException ex) {
        String errorCode = String.valueOf(INTERNAL_SERVER_ERROR.value());
        DefaultExceptionInfo info = new DefaultExceptionInfo();
        info.setErrorMessage(messageSource
                .getMessage(INTERNAL_SERVER_ERROR_MESSAGE, null, LocaleContextHolder.getLocale()));
        info.setErrorCode(errorCode);
        return new ResponseEntity<>(
                info, new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }


}
