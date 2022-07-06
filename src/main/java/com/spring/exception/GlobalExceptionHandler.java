package com.spring.exception;

import com.spring.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),String.valueOf(HttpStatus.NOT_FOUND),Instant.now()),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        Map<String,String> response=new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName=((FieldError)error).getField();
            String message= error.getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleNotReadableException(MethodArgumentTypeMismatchException exception)
    {
        return new ResponseEntity<>(new ApiResponse("id must be integer type",String.valueOf(HttpStatus.BAD_REQUEST), Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AdminCodeMismatchException.class)
    public ResponseEntity<ApiResponse> handleAdminCodeNotMatchException(AdminCodeMismatchException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleEmailExistException(EmailAlreadyExistException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),String.valueOf(HttpStatus.CONFLICT),Instant.now()),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotAvailableException(EmailNotFoundException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RoleNotAvailableException.class)
    public ResponseEntity<ApiResponse> handleRoleNotAvailableException(RoleNotAvailableException exception)
    {
        return new ResponseEntity<>(new ApiResponse(exception.getMessage(),String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
    }
}