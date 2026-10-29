package org.example;

/**
 * Árfolyam-szolgáltatás (éles környezetben pl. egy bank vagy egy árfolyam-API).
 *
 * <p>A tesztekben Mockito-mock helyettesíti, előre beállított árfolyamokkal.
 */
public interface ExchangeRateService {

    /**
     * Megadja, hogy 1 egység {@code from} deviza hány egység {@code to} devizát ér.
     *
     * @param from forrásdeviza (pl. "EUR")
     * @param to   céldeviza (pl. "HUF")
     */
    double getRate(String from, String to);
}
