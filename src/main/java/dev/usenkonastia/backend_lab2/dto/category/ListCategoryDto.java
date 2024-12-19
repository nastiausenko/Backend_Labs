package dev.usenkonastia.backend_lab2.dto.category;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class ListCategoryDto {
    List<CategoryEntryDto> categories;
}
