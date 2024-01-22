/*
 * Copyright (c) 2024. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.advice;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EntityExistsAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = EntityExistsException.class)
    public ResponseEntity<?> handleEntityExists(EntityExistsException exception) {
        return ResponseEntity.status(409).body(exception.getMessage());
    }
}
