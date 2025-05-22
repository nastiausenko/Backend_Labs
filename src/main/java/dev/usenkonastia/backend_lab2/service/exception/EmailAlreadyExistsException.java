package dev.usenkonastia.backend_lab2.service.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE = "Email %s is already in use";
    public EmailAlreadyExistsException(String email) {
        super(String.format(MESSAGE, email));
    }
}
