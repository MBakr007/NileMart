package org.route.nilemartspringboot.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.route.nilemartspringboot.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ErrorResponse> handleException(CartException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .status(ex.getHttpStatus())
                        .path(request.getRequestURI())
                        .error(request.getMethod())
                        .build(),
                ex.getHttpStatus());
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ErrorResponse> handleCategoryException(CategoryException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .status(ex.getStatus())
                        .path(request.getRequestURI())
                        .error(request.getMethod())
                        .build(),
                ex.getStatus());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(ProductException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .status(ex.getHttpStatus())
                        .path(request.getRequestURI())
                        .error(request.getMethod())
                        .build(),
                ex.getHttpStatus());
    }

}
