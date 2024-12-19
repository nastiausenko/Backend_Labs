package dev.usenkonastia.backend_lab2.dto.record;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;

@Value
@Builder
@Jacksonized
public class RecordDto {

    @NotNull
    Double expense;

    @NotNull
    ZonedDateTime date;
}
