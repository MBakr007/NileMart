package org.route.nilemartspringboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends RuntimeException{

    private final HttpStatus httpStatus;
    public ProductException(String message,  HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
