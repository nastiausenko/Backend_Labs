package dev.usenkonastia.backend_lab2.service.strategy;

import dev.usenkonastia.backend_lab2.dto.currency.PrivatbankCurrencyDto;
import dev.usenkonastia.backend_lab2.service.strategy.client.PrivatbankClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("privatbank")
@RequiredArgsConstructor
public class PrivatbankConversionStrategy implements CurrencyConversionStrategy {

    private final PrivatbankClient client;

    @Override
    public double convert(double amount, String from, String to) {
        if (from.equalsIgnoreCase(to)) return amount;

        List<PrivatbankCurrencyDto> rates = client.getRates();

        if (from.equalsIgnoreCase("UAH")) {
            return convertFromUah(amount, to, rates);
        } else if (to.equalsIgnoreCase("UAH")) {
            return convertToUah(amount, from, rates);
        } else {
            double uah = convertToUah(amount, from, rates);
            return convertFromUah(uah, to, rates);
        }
    }

    private double convertFromUah(double amount, String to, List<PrivatbankCurrencyDto> rates) {
        return rates.stream()
                .filter(r -> r.getCcy().equalsIgnoreCase(to) && r.getBase_ccy().equalsIgnoreCase("UAH"))
                .findFirst()
                .map(rate -> amount / rate.getBuy())
                .orElseThrow(() -> new RuntimeException("Rate not found for currency: " + to));
    }

    private double convertToUah(double amount, String from, List<PrivatbankCurrencyDto> rates) {
        return rates.stream()
                .filter(r -> r.getCcy().equalsIgnoreCase(from) && r.getBase_ccy().equalsIgnoreCase("UAH"))
                .findFirst()
                .map(rate -> amount * rate.getSale())
                .orElseThrow(() -> new RuntimeException("Rate not found for currency: " + from));
    }
}
