package dev.usenkonastia.backend_lab2.service.exception;

public class ForbiddenException extends RuntimeException {
    private static final String MESSAGE = "Access denied";
    public ForbiddenException() {
        super(MESSAGE);
    }
}
