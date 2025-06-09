package dev.usenkonastia.backend_lab2.service.strategy;

import dev.usenkonastia.backend_lab2.domain.currency.BankProvider;
import dev.usenkonastia.backend_lab2.service.factory.CurrencyConversionStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyConversionContext {

    private final CurrencyConversionStrategyFactory strategyFactory;

    public double convert(double amount, String from, String to, String bankProviderCode) {
        BankProvider bankProvider = BankProvider.fromCode(bankProviderCode);
        CurrencyConversionStrategy strategy = strategyFactory.createStrategy(bankProvider);
        return strategy.convert(amount, from, to);
    }
}