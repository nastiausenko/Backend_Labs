package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.domain.currency.CurrencyConversion;
import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionRequestDto;
import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionResponseDto;

import dev.usenkonastia.backend_lab2.service.CurrencyConversionService;
import dev.usenkonastia.backend_lab2.service.mapper.CurrencyConversionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyConversionMapper currencyMapper;
    private final CurrencyConversionService currencyConversionService;

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponseDto> convertCurrency(
            @Valid @RequestBody CurrencyConversionRequestDto request,
            @RequestParam(defaultValue = "monobank") String provider) {
        CurrencyConversion conversion = currencyMapper.toCurrencyConversion(request);
        conversion = currencyConversionService.convertCurrency(conversion, provider);

        return ResponseEntity.ok(currencyMapper.toCurrencyConversionResponseDto(conversion));
    }
}