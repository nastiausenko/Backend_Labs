package dev.usenkonastia.backend_lab2.domain.currency;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyConversion {
    String fromCurrency;
    String toCurrency;
    Double amount;
    Double result;
}
