package dev.usenkonastia.backend_lab2.service.currency;

import dev.usenkonastia.backend_lab2.domain.currency.BankProvider;
import dev.usenkonastia.backend_lab2.service.factory.CurrencyConversionStrategyFactory;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionContext;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Currency Conversion Context Tests")
class CurrencyConversionContextTest {

    @Mock
    private CurrencyConversionStrategyFactory factory;

    @Mock
    private CurrencyConversionStrategy strategy;

    @InjectMocks
    private CurrencyConversionContext context;

    @Test
    void testUseCorrectStrategyMonobank() {
        when(factory.createStrategy(BankProvider.MONO)).thenReturn(strategy);
        when(strategy.convert(100.0, "USD", "UAH")).thenReturn(3900.0);

        double result = context.convert(100.0, "USD", "UAH", "monobank");

        assertEquals(3900.0, result);
        verify(factory).createStrategy(BankProvider.MONO);
        verify(strategy).convert(100.0, "USD", "UAH");
    }

    @Test
    void testUseCorrectStrategyPrivatbank() {
        when(factory.createStrategy(BankProvider.PRIVAT)).thenReturn(strategy);
        when(strategy.convert(100.0, "USD", "UAH")).thenReturn(3900.0);

        double result = context.convert(100.0, "USD", "UAH", "privatbank");

        assertEquals(3900.0, result);
        verify(factory).createStrategy(BankProvider.PRIVAT);
        verify(strategy).convert(100.0, "USD", "UAH");
    }

    @Test
    void testUseCorrectStrategyNBU() {
        when(factory.createStrategy(BankProvider.NBU)).thenReturn(strategy);
        when(strategy.convert(100.0, "USD", "UAH")).thenReturn(3900.0);

        double result = context.convert(100.0, "USD", "UAH", "nbu");

        assertEquals(3900.0, result);
        verify(factory).createStrategy(BankProvider.NBU);
        verify(strategy).convert(100.0, "USD", "UAH");
    }

    @Test
    void testUnknownBankCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            context.convert(100.0, "USD", "UAH", "unknown");
        });
    }
}