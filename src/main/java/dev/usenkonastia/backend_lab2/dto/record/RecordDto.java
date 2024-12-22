package dev.usenkonastia.backend_lab2.dto.record;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class RecordDto {

    @NotNull
    UUID categoryId;

    @NotNull
    Double expense;
}
