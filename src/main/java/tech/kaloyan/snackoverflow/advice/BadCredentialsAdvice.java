/*
 * Copyright (c) 2024. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BadCredentialsAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
