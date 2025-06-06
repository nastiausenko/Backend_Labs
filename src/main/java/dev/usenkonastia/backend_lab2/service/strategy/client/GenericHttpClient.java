package dev.usenkonastia.backend_lab2.service.strategy.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class GenericHttpClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> List<T> fetchData(String url, Class<T[]> responseType, String sourceName) {
        try {
            T[] responseArray = restTemplate.getForObject(url, responseType);
            if (responseArray == null) {
                throw new RuntimeException(sourceName + " returned null");
            }
            return Arrays.asList(responseArray);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data from " + sourceName, e);
        }
    }
}
