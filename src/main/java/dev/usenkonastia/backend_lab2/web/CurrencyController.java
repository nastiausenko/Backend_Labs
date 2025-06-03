package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionRequestDto;
import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionResponseDto;

import dev.usenkonastia.backend_lab2.service.CurrencyConversionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    
    private final CurrencyConversionService currencyConversionService;
    
    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponseDto> convertCurrency(
            @Valid @RequestBody CurrencyConversionRequestDto request,
            @RequestParam(defaultValue = "mono") String provider) {
        
        CurrencyConversionResponseDto response = currencyConversionService
            .convertCurrency(request, provider);
        
        return ResponseEntity.ok(response);
    }
}