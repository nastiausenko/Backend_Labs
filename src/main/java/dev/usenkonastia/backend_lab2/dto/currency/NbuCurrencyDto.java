package dev.usenkonastia.backend_lab2.dto.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NbuCurrencyDto {
    @JsonProperty("r030")
    private int currencyCode;
    private String cc; // Alpha code
    private double rate;
    private String exchangedate;
}
