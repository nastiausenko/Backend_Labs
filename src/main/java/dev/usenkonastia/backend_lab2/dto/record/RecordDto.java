package dev.usenkonastia.backend_lab2.dto.record;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Kiev")
    ZonedDateTime date;
}
