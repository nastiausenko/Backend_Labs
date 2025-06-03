package dev.usenkonastia.backend_lab2.service.strategy.client;

import dev.usenkonastia.backend_lab2.dto.currency.MonobankCurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MonobankClient {
    private final RestTemplate restTemplate;
    private static final String MONOBANK_API_URL = "https://api.monobank.ua/bank/currency";

    public List<MonobankCurrencyDto> getCurrencyRates() {
        try {
            MonobankCurrencyDto[] rates = restTemplate.getForObject(MONOBANK_API_URL, MonobankCurrencyDto[].class);
            
            if (rates != null) {
                return Arrays.asList(rates);
            } else {
                throw new RuntimeException("Received null response from Monobank API");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch currency rates from Monobank", e);
        }
    }
}