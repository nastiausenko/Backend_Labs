package dev.usenkonastia.backend_lab2.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;


@Value
@Builder
public class Category {
    UUID id;
    UUID userId;
    String categoryName;
    Boolean isPublic;
    List<Record> records;
}
