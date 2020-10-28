package com.epam.esm.controller;

import com.epam.esm.exception.DefaultExceptionInfo;
import com.epam.esm.exception.InvalidUserDataException;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

    private static final String RESOURCE_NOT_FOUND = "Requested resource not found (id = %s)";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String INVALID_DATA_ERROR_MESSAGE = "Invalid user input";
    private static final String RESOURCE_ALREADY_EXISTS = "Created resource already exists (id = %s)";

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
        info.setErrorMessage(String.format(RESOURCE_NOT_FOUND, ex.getId()));
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
        info.setErrorMessage(String.format(RESOURCE_ALREADY_EXISTS, ex.getId()));
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
        String errorCode = String.valueOf(BAD_REQUEST.value());
        DefaultExceptionInfo info = new DefaultExceptionInfo();
        info.setErrorMessage(INVALID_DATA_ERROR_MESSAGE);
        info.setErrorCode(errorCode);
        return new ResponseEntity<>(
                info, new HttpHeaders(), BAD_REQUEST);
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
        info.setErrorMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        info.setErrorCode(errorCode);
        return new ResponseEntity<>(
                info, new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

}
