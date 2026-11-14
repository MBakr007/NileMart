package org.route.nilemartspringboot.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.route.nilemartspringboot.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ProductException.class, CartException.class})
    public ResponseEntity<ErrorResponse> handleException(ProductException ex, HttpServletRequest request) {
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
