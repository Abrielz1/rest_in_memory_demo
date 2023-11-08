package com.example.rest.rest.web.controller;

import com.example.rest.rest.exceptions.MethodArgumentNotValidException;
import com.example.rest.rest.exceptions.ObjectNotFoundException;
import com.example.rest.rest.web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNotFoundException(ObjectNotFoundException exception) {
        log.warn("404 {}", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getLocalizedMessage()));
    }

//    @ExceptionHandler(MethodArgumentNotValidException .class)
//    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException exception) {
//        log.warn("403 {}", exception.getLocalizedMessage());
//        BindingResult result = exception.getBindingResult()();
//        return List<String> errorMessages =
//    }
}
