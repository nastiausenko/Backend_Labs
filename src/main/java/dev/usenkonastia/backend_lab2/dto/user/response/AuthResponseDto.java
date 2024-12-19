package dev.usenkonastia.backend_lab2.dto.user.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AuthResponseDto {
    String accessToken;
}
