package dev.usenkonastia.backend_lab2.service.factory;

import dev.usenkonastia.backend_lab2.domain.currency.BankProvider;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionStrategy;

public interface CurrencyConversionStrategyFactory {
    CurrencyConversionStrategy createStrategy(BankProvider bankProvider);
}