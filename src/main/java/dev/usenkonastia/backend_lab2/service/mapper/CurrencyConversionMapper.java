package dev.usenkonastia.backend_lab2.service.mapper;

import dev.usenkonastia.backend_lab2.domain.currency.CurrencyConversion;
import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionRequestDto;
import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyConversionMapper {
    
    @Mapping(source = "fromCurrency", target = "fromCurrency")
    @Mapping(source = "toCurrency", target = "toCurrency")
    @Mapping(source = "amount", target = "amount")
    @Mapping(target = "result", ignore = true)
    CurrencyConversion toCurrencyConversion(CurrencyConversionRequestDto request);
    
    @Mapping(source = "fromCurrency", target = "fromCurrency")
    @Mapping(source = "toCurrency", target = "toCurrency")
    @Mapping(source = "amount", target = "originalAmount")
    @Mapping(source = "result", target = "convertedAmount")
    CurrencyConversionResponseDto toCurrencyConversionResponseDto(CurrencyConversion conversion);
}