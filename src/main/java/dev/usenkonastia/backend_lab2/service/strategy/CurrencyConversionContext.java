package dev.usenkonastia.backend_lab2.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyConversionContext {
    
    private final Map<String, CurrencyConversionStrategy> strategies;
    
    public double convert(double amount, String from, String to, String bankProvider) {
        CurrencyConversionStrategy strategy = strategies.get(bankProvider + "ConversionStrategy");
        
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown bank provider: " + bankProvider);
        }
        
        return strategy.convert(amount, from, to);
    }
}