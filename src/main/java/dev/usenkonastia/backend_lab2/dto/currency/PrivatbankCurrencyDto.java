package dev.usenkonastia.backend_lab2.dto.currency;

import lombok.Data;

@Data
public class PrivatbankCurrencyDto {
    private String ccy;
    private String base_ccy;
    private double buy;
    private double sale;
}
