package dev.usenkonastia.backend_lab2.service.strategy.client;

import dev.usenkonastia.backend_lab2.dto.currency.MonobankCurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MonobankClient {
    private final GenericHttpClient httpClient;
    private static final String URL = "https://api.monobank.ua/bank/currency";

    @Cacheable("monobankRates")
    public List<MonobankCurrencyDto> getRates() {
        return httpClient.fetchData(URL, MonobankCurrencyDto[].class, "Monobank");
    }
}
