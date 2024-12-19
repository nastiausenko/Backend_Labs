package dev.usenkonastia.backend_lab2.domain;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
@Builder
public class Record {
    UUID id;
    String userId;
    String categoryId;
    ZonedDateTime date;
    Double expense;
}
