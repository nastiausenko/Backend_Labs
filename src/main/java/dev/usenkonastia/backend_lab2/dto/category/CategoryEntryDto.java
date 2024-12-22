package dev.usenkonastia.backend_lab2.dto.category;

import dev.usenkonastia.backend_lab2.dto.record.RecordEntryDto;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class CategoryEntryDto {
    UUID id;
    UUID userId;
    String categoryName;
    Boolean isPublic;
    List<RecordEntryDto> records;
}
