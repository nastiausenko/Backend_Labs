package dev.usenkonastia.backend_lab2.service.strategy.client;

import dev.usenkonastia.backend_lab2.dto.currency.PrivatbankCurrencyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PrivatbankClient {

    private final RestTemplate restTemplate;
    private static final String PRIVAT_API_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public List<PrivatbankCurrencyDto> getRates() {
        try {
            PrivatbankCurrencyDto[] rates = restTemplate.getForObject(PRIVAT_API_URL, PrivatbankCurrencyDto[].class);
            if (rates != null) {
                return Arrays.asList(rates);
            } else {
                throw new RuntimeException("Received null response from PrivatBank API");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch PrivatBank rates", e);
        }
    }
}
