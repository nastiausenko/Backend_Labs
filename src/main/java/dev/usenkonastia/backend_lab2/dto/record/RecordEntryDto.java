package dev.usenkonastia.backend_lab2.dto.record;


import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;
import java.util.UUID;


@Value
@Builder
@Jacksonized
public class RecordEntryDto {
    UUID id;
    UUID userId;
    UUID categoryId;
    Double expense;
    ZonedDateTime date;
}
