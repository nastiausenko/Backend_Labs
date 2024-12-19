package dev.usenkonastia.backend_lab2.dto.record;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class RecordDto {
    @NotNull
    UUID userId;

    @NotNull
    UUID categoryId;

    @NotNull
    Double expense;

    @NotNull
    ZonedDateTime date;
}
