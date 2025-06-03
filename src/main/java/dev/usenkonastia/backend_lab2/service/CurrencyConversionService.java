package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.currency.CurrencyConversion;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final CurrencyConversionContext conversionContext;

    public CurrencyConversion convertCurrency(CurrencyConversion conversion, String bankProvider) {
     double convertedAmount = conversionContext.convert(
                conversion.getAmount(),
                conversion.getFromCurrency(),
                conversion.getToCurrency(),
                bankProvider
        );

        return CurrencyConversion.builder()
                .fromCurrency(conversion.getFromCurrency())
                .toCurrency(conversion.getToCurrency())
                .amount(conversion.getAmount())
                .result(convertedAmount)
                .build();
    }
}