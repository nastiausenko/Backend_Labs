package dev.usenkonastia.backend_lab2.service.strategy;

import dev.usenkonastia.backend_lab2.domain.currency.CurrencyCode;
import dev.usenkonastia.backend_lab2.service.strategy.client.MonobankClient;
import dev.usenkonastia.backend_lab2.dto.currency.MonobankCurrencyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component("monobank")
@RequiredArgsConstructor
public class MonobankConversionStrategy implements CurrencyConversionStrategy {

    private final MonobankClient monobankClient;

    @Override
    public double convert(double amount, String from, String to) {
        if (from.equalsIgnoreCase(to)) {
            return amount;
        }

        int fromCode = getNumericCode(from);
        int toCode = getNumericCode(to);

        List<MonobankCurrencyDto> rates = monobankClient.getRates();

        if (fromCode == 980) {
            return convertFromUah(amount, toCode, rates);
        } else if (toCode == 980) {
            return convertToUah(amount, fromCode, rates);
        } else {
            return convertThroughUah(amount, fromCode, toCode, rates);
        }
    }

    private double convertFromUah(double amount, int toCode, List<MonobankCurrencyDto> rates) {
        MonobankCurrencyDto rate = findRate(toCode, rates)
                .orElseThrow(() -> new RuntimeException(
                        "Exchange rate not found for currency code: " + toCode));

        double exchangeRate = rate.getRateBuy() > 0 ? rate.getRateBuy() :
                (rate.getRateCross() != null ? rate.getRateCross() : rate.getRateSell());

        return amount / exchangeRate;
    }

    private double convertToUah(double amount, int fromCode, List<MonobankCurrencyDto> rates) {
        MonobankCurrencyDto rate = findRate(fromCode, rates)
                .orElseThrow(() -> new RuntimeException(
                        "Exchange rate not found for currency code: " + fromCode));

        double exchangeRate = rate.getRateSell() > 0 ? rate.getRateSell() :
                (rate.getRateCross() != null ? rate.getRateCross() : rate.getRateBuy());

        return amount * exchangeRate;
    }

    private double convertThroughUah(double amount, int fromCode, int toCode, List<MonobankCurrencyDto> rates) {
        double uahAmount = convertToUah(amount, fromCode, rates);
        return convertFromUah(uahAmount, toCode, rates);
    }

    private Optional<MonobankCurrencyDto> findRate(int currencyCodeA, List<MonobankCurrencyDto> rates) {
        return rates.stream()
                .filter(rate -> (rate.getCurrencyCodeA() == currencyCodeA && rate.getCurrencyCodeB() == 980) ||
                                (rate.getCurrencyCodeA() == 980 && rate.getCurrencyCodeB() == currencyCodeA))
                .findFirst();
    }

    private int getNumericCode(String currencyCode) {
        return CurrencyCode.fromAlpha(currencyCode)
                .map(CurrencyCode::getNumericCode)
                .orElseThrow(() -> new RuntimeException("Unsupported currency: " + currencyCode));
    }
}
