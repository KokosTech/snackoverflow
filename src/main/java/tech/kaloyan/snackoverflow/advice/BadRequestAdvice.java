/*
 * Copyright (c) 2024. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tech.kaloyan.snackoverflow.exeception.BadRequestException;
import tech.kaloyan.snackoverflow.exeception.ConflictException;

@ControllerAdvice
public class BadRequestAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException exception) {
        return ResponseEntity.status(400).body(exception.getMessage());
    }
}
