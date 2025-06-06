package dev.usenkonastia.backend_lab2.service.strategy;

public interface CurrencyConversionStrategy {
    double convert(double amount, String from, String to);
}
