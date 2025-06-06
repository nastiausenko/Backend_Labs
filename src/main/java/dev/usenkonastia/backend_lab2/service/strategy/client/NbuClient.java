package dev.usenkonastia.backend_lab2.service.strategy.client;

import dev.usenkonastia.backend_lab2.dto.currency.NbuCurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NbuClient {
    private final GenericHttpClient httpClient;
    private static final String URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    @Cacheable("nbuRates")
    public List<NbuCurrencyDto> getRates() {
        return httpClient.fetchData(URL, NbuCurrencyDto[].class, "NBU");
    }
}
