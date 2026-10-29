package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("ExchangeRateService – beállított viselkedésű mock")
class ExchangeRateServiceTest {

    private static final double DELTA = 1e-9;

    private ExchangeRateService rateService;

    @BeforeEach
    void setUp() {
        rateService = mock(ExchangeRateService.class);
    }

    @Nested
    @DisplayName("Árfolyamok beállítása when().thenReturn()-nel")
    class StubbedRates {

        @Test
        @DisplayName("A mock a beállított EUR → HUF árfolyamot (400.0) adja vissza")
        void returnsStubbedRate() {
            when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);

            double rate = rateService.getRate("EUR", "HUF");

            assertEquals(400.0, rate, DELTA);
            verify(rateService).getRate("EUR", "HUF");
        }

        @Test
        @DisplayName("Különböző devizapárokra a hozzájuk beállított árfolyamot adja")
        void differentPairsHaveDifferentRates() {
            when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);
            when(rateService.getRate("USD", "HUF")).thenReturn(360.0);

            assertEquals(400.0, rateService.getRate("EUR", "HUF"), DELTA);
            assertEquals(360.0, rateService.getRate("USD", "HUF"), DELTA);
        }

        @Test
        @DisplayName("Be nem állított hívásra a mock az alapértéket (0.0) adja")
        void unstubbedCallReturnsDefault() {
            when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);

            assertEquals(0.0, rateService.getRate("GBP", "HUF"), DELTA);
        }

        @Test
        @DisplayName("Egymás utáni hívásokra egymás utáni értékek, utána az utolsó ismétlődik")
        void consecutiveStubbing() {
            when(rateService.getRate("EUR", "HUF")).thenReturn(400.0, 405.5, 410.0);

            assertEquals(400.0, rateService.getRate("EUR", "HUF"), DELTA);
            assertEquals(405.5, rateService.getRate("EUR", "HUF"), DELTA);
            assertEquals(410.0, rateService.getRate("EUR", "HUF"), DELTA);
            assertEquals(410.0, rateService.getRate("EUR", "HUF"), DELTA);
            verify(rateService, times(4)).getRate("EUR", "HUF");
        }
    }

    @Nested
    @DisplayName("A beállított árfolyam használata: CurrencyConverter")
    class ConverterWithStubbedRate {

        private CurrencyConverter converter;

        @BeforeEach
        void createConverter() {
            converter = new CurrencyConverter(rateService);
        }

        @Test
        @DisplayName("10 EUR = 4000 HUF a beállított 400.0-s árfolyammal")
        void convertsWithStubbedRate() {
            when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);

            assertEquals(4000.0, converter.convert(10, "EUR", "HUF"), DELTA);
            verify(rateService).getRate("EUR", "HUF");
            verifyNoMoreInteractions(rateService);
        }

        @Test
        @DisplayName("Az árfolyam változását követi")
        void followsRateChanges() {
            when(rateService.getRate("EUR", "HUF")).thenReturn(400.0, 410.0);

            assertEquals(4000.0, converter.convert(10, "EUR", "HUF"), DELTA);
            assertEquals(4100.0, converter.convert(10, "EUR", "HUF"), DELTA);
        }

        @Test
        @DisplayName("Érvénytelen árfolyamnál (0 vagy negatív) IllegalStateException")
        void invalidRateThrows() {
            when(rateService.getRate("EUR", "HUF")).thenReturn(-1.0);

            assertThrows(IllegalStateException.class, () -> converter.convert(10, "EUR", "HUF"));
            // GBP → HUF nincs beállítva, ezért a mock 0.0-t ad
            assertThrows(IllegalStateException.class, () -> converter.convert(10, "GBP", "HUF"));
        }

        @Test
        @DisplayName("Negatív összegnél hiba, és az árfolyam-szolgáltatás meg sem hívódik")
        void negativeAmountDoesNotCallService() {
            assertThrows(IllegalArgumentException.class, () -> converter.convert(-10, "EUR", "HUF"));

            verifyNoInteractions(rateService);
        }
    }
}
