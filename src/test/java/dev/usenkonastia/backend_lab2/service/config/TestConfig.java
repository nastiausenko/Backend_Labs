package dev.usenkonastia.backend_lab2.service.config;

import dev.usenkonastia.backend_lab2.service.strategy.MonobankConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.NbuConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.PrivatbankConversionStrategy;
import dev.usenkonastia.backend_lab2.service.strategy.client.GenericHttpClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public GenericHttpClient genericHttpClient() {
        return Mockito.mock(GenericHttpClient.class);
    }

    @Bean
    public MonobankConversionStrategy monobankConversionStrategy() {
        return Mockito.mock(MonobankConversionStrategy.class);
    }

    @Bean
    public PrivatbankConversionStrategy privatbankConversionStrategy() {
        return Mockito.mock(PrivatbankConversionStrategy.class);
    }

    @Bean
    public NbuConversionStrategy nbuConversionStrategy() {
        return Mockito.mock(NbuConversionStrategy.class);
    }
}