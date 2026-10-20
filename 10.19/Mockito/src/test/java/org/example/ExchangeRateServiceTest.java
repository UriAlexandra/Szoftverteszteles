package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateService rateService;

    @Test
    void testExchangeRateMocking() {
        // Arrange: Megmondjuk a mocknak, hogy mit válaszoljon
        when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);

        // Act: Lekérdezzük az értéket
        double rate = rateService.getRate("EUR", "HUF");

        // Assert: Ellenőrizzük, hogy valóban azt adta-e vissza, amit betanítottunk neki
        assertEquals(400.0, rate);
    }
}
