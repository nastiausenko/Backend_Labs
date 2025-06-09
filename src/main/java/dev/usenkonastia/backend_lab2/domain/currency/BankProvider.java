package dev.usenkonastia.backend_lab2.domain.currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankProvider {
    MONO("monobank"),
    PRIVAT("privatbank"),
    NBU("nbu");
    
    private final String code;
    
    public static BankProvider fromCode(String code) {
        for (BankProvider provider : values()) {
            if (provider.getCode().equalsIgnoreCase(code)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown bank provider: " + code);
    }
}