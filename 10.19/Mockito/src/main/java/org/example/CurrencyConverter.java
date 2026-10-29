package org.example;

import java.util.Objects;

/**
 * Devizaátváltás az {@link ExchangeRateService} árfolyamai alapján.
 *
 * <p>Az árfolyam-szolgáltatást konstruktoron keresztül kapja, így a tesztekben
 * mock adható át neki előre beállított árfolyamokkal.
 */
public class CurrencyConverter {

    private final ExchangeRateService exchangeRateService;

    /**
     * @param exchangeRateService az árfolyamokat adó szolgáltatás
     * @throws NullPointerException ha a szolgáltatás null
     */
    public CurrencyConverter(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = Objects.requireNonNull(exchangeRateService,
                "Az árfolyam-szolgáltatás nem lehet null!");
    }

    /**
     * Átváltja az összeget a {@code from} devizából a {@code to} devizába.
     *
     * @throws IllegalArgumentException ha az összeg negatív, NaN vagy végtelen
     * @throws IllegalStateException    ha a szolgáltatás érvénytelen árfolyamot ad
     *                                  (0, negatív, NaN vagy végtelen)
     */
    public double convert(double amount, String from, String to) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Az átváltandó összeg nem lehet negatív!");
        }
        double rate = exchangeRateService.getRate(from, to);
        if (!Double.isFinite(rate) || rate <= 0) {
            throw new IllegalStateException("Érvénytelen árfolyam: " + from + " → " + to + " = " + rate);
        }
        return amount * rate;
    }
}
