package dev.usenkonastia.backend_lab2.service.strategy.client;

import dev.usenkonastia.backend_lab2.dto.currency.NbuCurrencyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NbuClient {

    private final RestTemplate restTemplate;
    private static final String NBU_API_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public List<NbuCurrencyDto> getRates() {
        try {
            NbuCurrencyDto[] rates = restTemplate.getForObject(NBU_API_URL, NbuCurrencyDto[].class);

            if (rates != null) {
                return Arrays.asList(rates);
            } else {
                throw new RuntimeException("Received null response from NBU API");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch NBU rates", e);
        }
    }
}
