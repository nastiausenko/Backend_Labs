package dev.usenkonastia.backend_lab2.dto.user;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class UserEntryDto {
    UUID id;
    String name;
    String email;
}
