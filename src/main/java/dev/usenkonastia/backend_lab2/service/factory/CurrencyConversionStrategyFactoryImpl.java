package dev.usenkonastia.backend_lab2.service.factory;

import dev.usenkonastia.backend_lab2.domain.currency.BankProvider;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.MonobankConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.NbuConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.PrivatbankConversionStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrencyConversionStrategyFactoryImpl implements CurrencyConversionStrategyFactory {

    private final MonobankConversionStrategy monobankStrategy;
    private final PrivatbankConversionStrategy privatStrategy;
    private final NbuConversionStrategy nbuStrategy;

    private final Map<BankProvider, CurrencyConversionStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    private void init() {
        strategyMap.put(BankProvider.MONO, monobankStrategy);
        strategyMap.put(BankProvider.PRIVAT, privatStrategy);
        strategyMap.put(BankProvider.NBU, nbuStrategy);
    }

    @Override
    public CurrencyConversionStrategy createStrategy(BankProvider bankProvider) {
        CurrencyConversionStrategy strategy = strategyMap.get(bankProvider);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported bank provider: " + bankProvider);
        }
        return strategy;
    }
}