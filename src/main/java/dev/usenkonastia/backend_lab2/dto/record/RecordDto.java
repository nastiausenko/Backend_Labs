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
    Double expense;

    @NotNull
    UUID categoryId;

    @NotNull
    ZonedDateTime date;

    @NotNull
    UUID userId;
}
