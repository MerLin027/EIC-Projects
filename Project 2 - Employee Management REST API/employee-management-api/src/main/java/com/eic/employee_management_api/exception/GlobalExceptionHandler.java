package com.eic.employee_management_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// Generally, we would have to use try-catch for every method in the REST Controller class, resulting in a cluttered mess
// Which is why we're using a separate class for it
@RestControllerAdvice
// indicates that this class advices all Controllers
// When any of them throws an exception ask this class how to handle it
public class GlobalExceptionHandler
{
    @ExceptionHandler(ResourceNotFoundException.class)
    // When this type of exception is thrown, run this specific method
    public ResponseEntity <ErrorResponse> handleNotFound(ResourceNotFoundException e, HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                404,
                "Not Found",
                e.getMessage(),
                request.getRequestURI(),
                null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity <ErrorResponse> handleConflict(DuplicateResourceException e, HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        404,
                        "Not Found",
                        e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
