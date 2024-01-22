/*
 * Copyright (c) 2024. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tech.kaloyan.snackoverflow.exeception.ConflictException;


@ControllerAdvice
public class ConflictAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException exception) {
        return ResponseEntity.status(409).body(exception.getMessage());
    }
}
