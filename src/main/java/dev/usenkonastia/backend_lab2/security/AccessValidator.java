package dev.usenkonastia.backend_lab2.security;

import dev.usenkonastia.backend_lab2.service.exception.ForbiddenException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccessValidator {
    public void validateOwner(UUID currentUserId, UUID ownerId) {
        if (!currentUserId.equals(ownerId)) {
            throw new ForbiddenException();
        }
    }
}
