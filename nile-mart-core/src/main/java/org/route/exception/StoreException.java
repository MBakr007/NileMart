package org.route.exception;

/**
 * Raised whenever a NileMart business rule is violated (missing product,
 * insufficient stock, empty checkout, invalid quantity, ...).
 */
public class StoreException extends RuntimeException {

    public StoreException(String message) {
        super(message);
    }
}
