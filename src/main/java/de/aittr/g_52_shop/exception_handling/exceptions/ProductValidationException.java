package de.aittr.g_52_shop.exception_handling.exceptions;

public class ProductValidationException extends RuntimeException {

    public ProductValidationException(String message) {
        super(message);
    }

    public ProductValidationException(Throwable cause) {
        super(cause);
    }
}
