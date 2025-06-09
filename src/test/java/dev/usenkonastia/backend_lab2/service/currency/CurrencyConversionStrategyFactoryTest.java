package dev.usenkonastia.backend_lab2.service.currency;

import dev.usenkonastia.backend_lab2.domain.currency.BankProvider;
import dev.usenkonastia.backend_lab2.service.config.TestConfig;
import dev.usenkonastia.backend_lab2.service.factory.CurrencyConversionStrategyFactory;
import dev.usenkonastia.backend_lab2.service.factory.CurrencyConversionStrategyFactoryImpl;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.MonobankConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.NbuConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.PrivatbankConversionStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        CurrencyConversionStrategyFactoryImpl.class,
        TestConfig.class })
@DisplayName("Currency Conversion Factory Tests")
public class CurrencyConversionStrategyFactoryTest {

    @Autowired
    private CurrencyConversionStrategyFactory factory;

    @Test
    void testCreateStrategy_Monobank() {
        CurrencyConversionStrategy strategy = factory.createStrategy(BankProvider.MONO);
        assertNotNull(strategy);
        assertInstanceOf(MonobankConversionStrategy.class, strategy);
    }

    @Test
    void testCreateStrategy_Privatbank() {
        CurrencyConversionStrategy strategy = factory.createStrategy(BankProvider.PRIVAT);
        assertNotNull(strategy);
        assertInstanceOf(PrivatbankConversionStrategy.class, strategy);
    }

    @Test
    void testCreateStrategy_NBU() {
        CurrencyConversionStrategy strategy = factory.createStrategy(BankProvider.NBU);
        assertNotNull(strategy);
        assertInstanceOf(NbuConversionStrategy.class, strategy);
    }

    @Test
    void testCreateUnknownBankProvider() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createStrategy(null);
        });
    }
}