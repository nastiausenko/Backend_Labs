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
    Double expense;
    UUID categoryId;
    ZonedDateTime date;
    UUID userId;
}
