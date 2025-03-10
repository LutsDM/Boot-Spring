package de.aittr.g_52_shop.exception_handling.exceptions;

public class CustomerValidationException extends RuntimeException {
    public CustomerValidationException(String message) {
        super(message);
    }

    public CustomerValidationException(Throwable cause) {
        super(cause);
    }
}
