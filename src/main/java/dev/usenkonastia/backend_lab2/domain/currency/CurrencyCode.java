package dev.usenkonastia.backend_lab2.domain.currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum CurrencyCode {
    UAH("UAH", 980),
    USD("USD", 840),
    EUR("EUR", 978),
    GBP("GBP", 826),
    PLN("PLN", 985);

    private final String alphaCode;
    private final int numericCode;

    public static Optional<CurrencyCode> fromAlpha(String code) {
        for (CurrencyCode currency : values()) {
            if (currency.getAlphaCode().equalsIgnoreCase(code)) {
                return Optional.of(currency);
            }
        }
        return Optional.empty();
    }

    public static Optional<CurrencyCode> fromNumeric(int numericCode) {
        for (CurrencyCode currency : values()) {
            if (currency.getNumericCode() == numericCode) {
                return Optional.of(currency);
            }
        }
        return Optional.empty();
    }
}
