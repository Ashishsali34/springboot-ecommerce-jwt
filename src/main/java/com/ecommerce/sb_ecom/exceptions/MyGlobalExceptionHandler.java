package com.ecommerce.sb_ecom.exceptions;

import com.ecommerce.sb_ecom.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        // Field errors (safe cast; you only iterate field errors)
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                errors.put(fe.getField(), fe.getDefaultMessage())
        );

        // Global (object-level) errors (e.g., class-level constraints)
        ex.getBindingResult().getGlobalErrors().forEach(ge ->
                errors.put(ge.getObjectName(), ge.getDefaultMessage())
        );

        return errors; // @ResponseStatus sets 400
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

}
