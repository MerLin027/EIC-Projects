package com.eic.employee_management_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Generally, we would have to use try-catch for every method in the REST Controller class, resulting in a cluttered mess
// Which is why we're using a separate class for it
@RestControllerAdvice
// indicates that this class advices all Controllers
// When any of them throws an exception ask this class how to handle it
public class GlobalExceptionHandler
{
    @ExceptionHandler(ResourceNotFoundException.class)
    // When this type of exception is thrown, run this specific method
    public ResponseEntity <ErrorResponse> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        404,
                        "Not Found pls find elsewhere",
                        e.toString(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity <ErrorResponse> handleDuplicateResources(DuplicateResourceException e, HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity <ErrorResponse> handleInvalidJsonFormat(HttpMessageNotReadableException e, HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity <ErrorResponse> handleUnsupportedRequestMethod(HttpRequestMethodNotSupportedException e,
                                                                         HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        405,
                        "Method Not Allowed",
                        e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <ErrorResponse> handleRequestFieldValidation(MethodArgumentNotValidException e,
                                                                       HttpServletRequest request)
    {
        Map <String, String> details = new HashMap <> ();
        for (FieldError error : e.getBindingResult().getFieldErrors())
            details.put(error.getField(), error.getDefaultMessage());
        // When @Valid runs, the rules that fail are stored with an Exception attached to them
        // Spring packs them and throws the Exception
        // BindingResult -> Spring's complete report of the validation run
        // getField() -> field name
        // getFieldErrors() -> List of Errors from the BindingResult
        // getDefaultMessage() -> message from our validation tags
        // FieldError -> failed rule which is stored
        // A FieldError holds the following -> getField(), getDefaultMessage(), getRejectedValue(), getCode()
        // Note -> getCode() returns the validation tag used
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Request Validation Failed",
                        request.getRequestURI(),
                        details);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity <ErrorResponse> handleInvalidContentTypeRequest(HttpMediaTypeNotSupportedException e,
                                                                   HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        415,
                        "Unsupported Media Type",
                        e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity <ErrorResponse> handleInvalidPathVariable(MethodArgumentTypeMismatchException e,
                                                                   HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Invalid Path Variable",
                        request.getRequestURI(),
                        null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity <ErrorResponse> handleInvalidSortRequestParameter(PropertyReferenceException e,
                                                                   HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Invalid Sort Field",
                        request.getRequestURI(),
                        null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity <ErrorResponse> handleUnmappedResourceRequest(NoResourceFoundException e, HttpServletRequest request)
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
    @ExceptionHandler(Exception.class)
    public ResponseEntity <ErrorResponse> handleGenericException(Exception e, HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        500,
                        "Internal Server Error",
                        "Something went wrong: "+e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.internalServerError().body(response);
    }
}
