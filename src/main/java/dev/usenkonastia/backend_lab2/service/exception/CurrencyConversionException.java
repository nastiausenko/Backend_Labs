package dev.usenkonastia.backend_lab2.service.exception;

public class CurrencyConversionException extends RuntimeException {
    private static final String MESSAGE = "Exchange rate not found for currency code: %s";
    public CurrencyConversionException(String code) {
        super(String.format(MESSAGE, code));
    }
}