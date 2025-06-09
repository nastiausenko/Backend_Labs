package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.currency.CurrencyConversion;

public interface CurrencyConversionService {
    CurrencyConversion convertCurrency(CurrencyConversion conversion, String bankProvider);
}
