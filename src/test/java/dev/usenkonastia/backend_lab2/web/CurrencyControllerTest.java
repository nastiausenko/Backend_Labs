package dev.usenkonastia.backend_lab2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.usenkonastia.backend_lab2.dto.currency.CurrencyConversionRequestDto;
import dev.usenkonastia.backend_lab2.service.CurrencyConversionService;
import dev.usenkonastia.backend_lab2.service.mapper.CurrencyConversionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Currency Controller Tests")
@Testcontainers
public class CurrencyControllerTest extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private CurrencyConversionService currencyConversionService;

    @SpyBean
    private CurrencyConversionMapper currencyConversionMapper;

    private CurrencyConversionRequestDto requestDto;

    @BeforeEach
    void setUp() {
        reset(currencyConversionService, currencyConversionMapper);

        requestDto = CurrencyConversionRequestDto.builder()
                .fromCurrency("USD")
                .toCurrency("UAH")
                .amount(100.0)
                .build();
    }

    @Test
    @DisplayName("Currency conversion with valid data and provider")
    void testConvertCurrency() throws Exception {
        mockMvc.perform(post("/api/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .param("provider", "privatbank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.convertedAmount").isNotEmpty());
    }

    @Test
    @DisplayName("Currency conversion - invalid input returns 400")
    void testConvertCurrency_InvalidInput() throws Exception {
        CurrencyConversionRequestDto invalidDto = CurrencyConversionRequestDto.builder()
                .fromCurrency("") // Invalid field
                .toCurrency("UAH")
                .amount(15.0)
                .build();

        mockMvc.perform(post("/api/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}