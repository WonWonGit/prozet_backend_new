package com.example.prozet.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExecptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<?> handlerCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
    
}
