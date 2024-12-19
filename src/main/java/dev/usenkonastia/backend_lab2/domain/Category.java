package dev.usenkonastia.backend_lab2.domain;

import dev.usenkonastia.backend_lab2.dto.record.RecordDto;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;


@Value
@Builder
public class Category {
    UUID id;
    String categoryName;
    Boolean isPublic;
    List<RecordDto> records;
}
