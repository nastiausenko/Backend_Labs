package dev.usenkonastia.backend_lab2.service.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Category with id %s not found";
    public CategoryNotFoundException(UUID id) {
        super(String.format(MESSAGE, id));
    }
}
