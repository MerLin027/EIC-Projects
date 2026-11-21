package com.eic.product_catalogue_api.product.exception;

import com.eic.product_catalogue_api.product.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
// Generally, we would have to use try-catch for every method in the REST Controller classes, resulting in a cluttered mess
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
                        "Not Found",
                        e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity <ErrorResponse> handleDuplicateResources(DuplicateResourceException e, HttpServletRequest request)
    {
        // The SRS asks for 400 on a duplicate category name (and we use the same for a duplicate email)
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
                        "Malformed or invalid JSON request body",
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
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors())
            details.putIfAbsent(error.getField(), error.getDefaultMessage());
        // When @Valid runs, the rules that fail are stored and Spring throws this Exception
        // BindingResult -> Spring's complete report of the validation run
        // getFieldErrors() -> List of FieldErrors, one per failed rule
        // getField() -> field name (a nested field shows up as "category.name")
        // getDefaultMessage() -> message from our validation tags
        // putIfAbsent -> if one field fails two rules, we keep only the first message
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Request Validation Failed",
                        request.getRequestURI(),
                        details);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity <ErrorResponse> handleRequestParamValidation(ConstraintViolationException e,
                                                                       HttpServletRequest request)
    {
        // Thrown when a constraint on a @RequestParam / @PathVariable fails, e.g. ?minPrice=-5
        // Needs @Validated on the controller class, otherwise those annotations are not checked
        Map <String, String> details = new LinkedHashMap <> ();
        for (ConstraintViolation<?> violation : e.getConstraintViolations())
            details.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage());
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Request Validation Failed",
                        request.getRequestURI(),
                        details);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity <ErrorResponse> handleMissingRequestParam(MissingServletRequestParameterException e,
                                                                    HttpServletRequest request)
    {
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Required parameter '" + e.getParameterName() + "' is missing",
                        request.getRequestURI(),
                        null);
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
                        "Invalid value '" + e.getValue() + "' for parameter '" + e.getName() + "'",
                        request.getRequestURI(),
                        null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity <ErrorResponse> handleUnmappedResourceRequest(NoResourceFoundException e, HttpServletRequest request)
    {
        // Thrown when the URL doesn't match any controller
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        404,
                        "Not Found",
                        e.getMessage(),
                        request.getRequestURI(),
                        null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity <ErrorResponse> handleInvalidSortField(PropertyReferenceException e,
                                                                 HttpServletRequest request)
    {
        // Thrown for something like ?sort=banana,asc where Product has no such field
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Invalid sort field '" + e.getPropertyName() + "'",
                        request.getRequestURI(),
                        null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity <ErrorResponse> handlesDuplicateResourceForConcurrentRequest(DataIntegrityViolationException e,
                                                                                       HttpServletRequest request)
    {
        // Safety net: the service already checks for duplicates, but two identical requests at the same instant
        // can both pass the check, and the database's unique constraint rejects the second one
        // Same status as DuplicateResourceException, so the client sees one consistent answer
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        400,
                        "Bad Request",
                        "Request conflicts with existing data",
                        request.getRequestURI(),
                        null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity <ErrorResponse> handleFailedLogin(AuthenticationException e, HttpServletRequest request)
    {
        // Thrown by authenticationManager.authenticate() inside AuthService.verify() on a bad email or password
        // This happens INSIDE the controller flow, so this class can catch it
        // The message is deliberately vague: it doesn't reveal whether the email exists
        // Missing/expired/invalid tokens on protected URLs never reach here, JwtAuthenticationEntryPoint handles those
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        401,
                        "Unauthorized",
                        "Invalid email or password",
                        request.getRequestURI(),
                        null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity <ErrorResponse> handleGenericException(Exception e, HttpServletRequest request)
    {
        // Last resort for anything unexpected
        // The real cause goes to the log; the client only sees a generic message (no stack traces leaked)
        log.error("Unexpected error on {}", request.getRequestURI(), e);
        ErrorResponse response = new ErrorResponse
                (LocalDateTime.now(),
                        500,
                        "Internal Server Error",
                        "Something went wrong",
                        request.getRequestURI(),
                        null);
        return ResponseEntity.internalServerError().body(response);
    }
}
