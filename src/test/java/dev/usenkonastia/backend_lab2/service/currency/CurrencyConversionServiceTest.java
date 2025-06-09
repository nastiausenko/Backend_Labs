package dev.usenkonastia.backend_lab2.service.currency;

import dev.usenkonastia.backend_lab2.domain.currency.CurrencyConversion;
import dev.usenkonastia.backend_lab2.service.impl.CurrencyConversionServiceImpl;
import dev.usenkonastia.backend_lab2.service.strategy.CurrencyConversionContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CurrencyConversionServiceImpl.class)
@DisplayName("Currency Conversion Service Tests")
public class CurrencyConversionServiceTest {

    @MockBean
    private CurrencyConversionContext context;

    @Autowired
    private CurrencyConversionServiceImpl currencyConversionService;

    @Test
    void testConvertCurrency() {
        CurrencyConversion conversion = CurrencyConversion.builder()
                .amount(100.0)
                .fromCurrency("USD")
                .toCurrency("EUR")
                .build();

        String bank = "monobank";

        when(context.convert(100.0, "USD", "EUR", bank)).thenReturn(93.5);

        CurrencyConversion result = currencyConversionService.convertCurrency(conversion, bank);

        assertEquals("USD", result.getFromCurrency());
        assertEquals("EUR", result.getToCurrency());
        assertEquals(100.0, result.getAmount());
        assertEquals(93.5, result.getResult());

        verify(context, times(1)).convert(100.0, "USD", "EUR", bank);
    }
}
