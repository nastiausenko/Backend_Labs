package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionRequestDto;
import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionResponseDto;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConversionService {
    
    private final CurrencyConversionContext conversionContext;
    
    public CurrencyConversionResponseDto convertCurrency(CurrencyConversionRequestDto request, String bankProvider) {
        double convertedAmount = conversionContext.convert(
            request.getAmount(),
            request.getFromCurrency(),
            request.getToCurrency(),
            bankProvider
        );

        return CurrencyConversionResponseDto.builder()
            .fromCurrency(request.getFromCurrency())
            .toCurrency(request.getToCurrency())
            .originalAmount(request.getAmount())
            .convertedAmount(convertedAmount)
            .build();
    }
}