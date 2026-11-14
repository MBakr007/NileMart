package org.route.nilemartspringboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CategoryNotFoundException extends RuntimeException {
    private final HttpStatus status;
    public CategoryNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
