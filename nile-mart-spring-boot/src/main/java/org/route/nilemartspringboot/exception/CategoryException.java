package org.route.nilemartspringboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CategoryException extends RuntimeException {
    private final HttpStatus status;
    public CategoryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
