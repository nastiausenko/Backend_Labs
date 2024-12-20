package dev.usenkonastia.backend_lab2.dto.category;

import dev.usenkonastia.backend_lab2.dto.record.RecordDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class CategoryDto {

    @NotNull(message = "User id cannot be null")
    UUID userId;

    @NotBlank(message = "Category name cannot be blank")
    String categoryName;

    @NotNull(message = "Public status cannot be null")
    Boolean isPublic;

    List<RecordDto> records;
}
