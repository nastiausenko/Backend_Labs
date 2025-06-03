package dev.usenkonastia.backend_lab2.service.strategy.client;

import dev.usenkonastia.backend_lab2.dto.currency.MonobankCurrencyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class MonobankClient {
    private final RestTemplate restTemplate;
    private static final String MONOBANK_API_URL = "https://api.monobank.ua/bank/currency";
    
    public MonobankClient() {
        this.restTemplate = new RestTemplate();
    }
    
    public List<MonobankCurrencyDto> getCurrencyRates() {
        try {
            log.info("Fetching currency rates from Monobank API");
            MonobankCurrencyDto[] rates = restTemplate.getForObject(MONOBANK_API_URL, MonobankCurrencyDto[].class);
            
            if (rates != null) {
                List<MonobankCurrencyDto> ratesList = Arrays.asList(rates);
                log.info("Successfully fetched {} currency rates", ratesList.size());
                return ratesList;
            } else {
                throw new RuntimeException("Received null response from Monobank API");
            }
        } catch (RestClientException e) {
            log.error("Error fetching currency rates from Monobank", e);
            throw new RuntimeException("Failed to fetch currency rates from Monobank", e);
        }
    }
}