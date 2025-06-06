package dev.usenkonastia.backend_lab2.dto.currency;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyConversionRequestDto {
    String fromCurrency;
    String toCurrency;
    Double amount;
}
