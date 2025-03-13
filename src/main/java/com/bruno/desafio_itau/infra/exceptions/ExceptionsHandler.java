package com.bruno.desafio_itau.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception ex) {
        return ResponseEntity.internalServerError().build();
    }

}
