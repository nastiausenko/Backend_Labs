package dev.usenkonastia.backend_lab2.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonobankCurrencyDto {
    private int currencyCodeA;
    private int currencyCodeB;
    private long date;
    private double rateSell;
    private double rateBuy;
    private Double rateCross;
}
