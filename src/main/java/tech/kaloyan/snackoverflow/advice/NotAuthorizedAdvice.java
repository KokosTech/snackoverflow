/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;

@ControllerAdvice
public class NotAuthorizedAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(NotAuthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
