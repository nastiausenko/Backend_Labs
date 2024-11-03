package dev.usenkonastia.backend_lab2.service.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "User with id %s not found";

    public UserNotFoundException(UUID id) {
        super(String.format(MESSAGE, id));
    }
}
