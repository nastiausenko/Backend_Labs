package dev.usenkonastia.backend_lab2.service.exception;

public class InvalidArgumentsException extends RuntimeException {
    public InvalidArgumentsException(String message) {
        super(message);
    }
}
