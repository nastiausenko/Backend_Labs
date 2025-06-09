package dev.usenkonastia.backend_lab2.service.strategy.client;

import dev.usenkonastia.backend_lab2.dto.currency.PrivatbankCurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PrivatbankClient {
    private final GenericHttpClient httpClient;
    private static final String URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    @Cacheable("privatbankRates")
    public List<PrivatbankCurrencyDto> getRates() {
        return httpClient.fetchData(URL, PrivatbankCurrencyDto[].class, "PrivatBank");
    }
}
