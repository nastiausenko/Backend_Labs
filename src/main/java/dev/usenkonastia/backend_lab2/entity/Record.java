package dev.usenkonastia.backend_lab2.entity;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Value
public class Record {
    UUID id;
    String userId;
    String categoryId;
    LocalDateTime date;
    Double expense;
}
