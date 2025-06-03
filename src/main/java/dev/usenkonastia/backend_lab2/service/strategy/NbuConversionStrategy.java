package dev.usenkonastia.backend_lab2.service.strategy;

import dev.usenkonastia.backend_lab2.domain.currency.CurrencyCode;
import dev.usenkonastia.backend_lab2.dto.currency.NbuCurrencyDto;
import dev.usenkonastia.backend_lab2.service.strategy.client.NbuClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component("nbu")
@RequiredArgsConstructor
public class NbuConversionStrategy implements CurrencyConversionStrategy {

    private final NbuClient nbuClient;

    @Override
    public double convert(double amount, String from, String to) {
        if (from.equalsIgnoreCase(to)) return amount;

        List<NbuCurrencyDto> rates = nbuClient.getRates();

        double fromRate = from.equalsIgnoreCase("UAH") ? 1.0 : findRate(from, rates);
        double toRate = to.equalsIgnoreCase("UAH") ? 1.0 : findRate(to, rates);

        return amount * fromRate / toRate;
    }

    private double findRate(String currency, List<NbuCurrencyDto> rates) {
        int numericCode = CurrencyCode.fromAlpha(currency)
                .orElseThrow(() -> new RuntimeException("Unsupported currency: " + currency))
                .getNumericCode();

        return rates.stream()
                .filter(rate -> rate.getCurrencyCode() == numericCode)
                .findFirst()
                .map(NbuCurrencyDto::getRate)
                .orElseThrow(() -> new RuntimeException("Rate not found for currency: " + currency));
    }
}
