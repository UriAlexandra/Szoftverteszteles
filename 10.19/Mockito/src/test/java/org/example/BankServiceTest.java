package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DisplayName("BankService – mockolt értesítési szolgáltatással")
class BankServiceTest {

    private static final double DELTA = 1e-9;

    private NotificationService notificationService;
    private BankService bankService;
    private BankAccount account;

    @BeforeEach
    void setUp() {
        // Valódi értesítési szolgáltatás helyett mock: nem megy ki valódi üzenet
        notificationService = mock(NotificationService.class);
        // Dependency Injection: a mockot konstruktoron keresztül kapja a BankService
        bankService = new BankService(notificationService);
        account = new BankAccount();
    }

    @Nested
    @DisplayName("A feladatban előírt esetek")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class RequiredCases {

        @Test
        @Order(1)
        @DisplayName("1. A befizetés megfelelően növeli a számla egyenlegét")
        void depositIncreasesBalance() {
            bankService.deposit(account, 1000);

            assertEquals(1000.0, account.getBalance(), DELTA);
        }

        @Test
        @Order(2)
        @DisplayName("2. Sikeres befizetés után meghívódik a send() metódus")
        void successfulDepositCallsSend() {
            bankService.deposit(account, 1000);

            verify(notificationService, times(1)).send(anyString());
        }

        @Test
        @Order(3)
        @DisplayName("3. verify(): a megfelelő értesítés megy ki")
        void correctNotificationIsSent() {
            bankService.deposit(account, 1000);

            verify(notificationService).send("Sikeres befizetés: 1000.00 Ft. Új egyenleg: 1000.00 Ft.");
            verifyNoMoreInteractions(notificationService);
        }

        @ParameterizedTest(name = "befizetés: {0}")
        @ValueSource(doubles = {0, -100, Double.NaN})
        @Order(4)
        @DisplayName("4. Sikertelen befizetés esetén nem történik értesítés")
        void failedDepositSendsNoNotification(double amount) {
            assertThrows(IllegalArgumentException.class, () -> bankService.deposit(account, amount));

            verify(notificationService, never()).send(anyString());
            verifyNoInteractions(notificationService);
            assertEquals(0.0, account.getBalance(), DELTA);
        }

        @Test
        @Order(5)
        @DisplayName("5. A teszt mockot használ, nem valódi értesítési szolgáltatást")
        void usesMockInsteadOfRealService() {
            assertTrue(mockingDetails(notificationService).isMock());
        }
    }

    @Nested
    @DisplayName("További esetek")
    class AdditionalCases {

        @Test
        @DisplayName("Minden befizetésről külön értesítés megy ki, az aktuális egyenleggel")
        void eachDepositSendsItsOwnNotification() {
            bankService.deposit(account, 500);
            bankService.deposit(account, 250);

            verify(notificationService).send("Sikeres befizetés: 500.00 Ft. Új egyenleg: 500.00 Ft.");
            verify(notificationService).send("Sikeres befizetés: 250.00 Ft. Új egyenleg: 750.00 Ft.");
            verify(notificationService, times(2)).send(anyString());
            verifyNoMoreInteractions(notificationService);
        }

        @Test
        @DisplayName("Sikeres és sikertelen befizetés után csak a sikeresről megy értesítés")
        void onlySuccessfulDepositsAreNotified() {
            bankService.deposit(account, 1000);
            assertThrows(IllegalArgumentException.class, () -> bankService.deposit(account, -5));

            verify(notificationService, times(1)).send(anyString());
            verify(notificationService).send("Sikeres befizetés: 1000.00 Ft. Új egyenleg: 1000.00 Ft.");
            assertEquals(1000.0, account.getBalance(), DELTA);
        }

        @Test
        @DisplayName("Tört összeg két tizedesjeggyel jelenik meg az értesítésben")
        void decimalAmountInMessage() {
            bankService.deposit(account, 100.5);

            verify(notificationService).send("Sikeres befizetés: 100.50 Ft. Új egyenleg: 100.50 Ft.");
        }

        @Test
        @DisplayName("null számla esetén NullPointerException, és nincs értesítés")
        void nullAccountSendsNoNotification() {
            assertThrows(NullPointerException.class, () -> bankService.deposit(null, 1000));

            verifyNoInteractions(notificationService);
        }

        @Test
        @DisplayName("A BankService nem hozható létre értesítési szolgáltatás nélkül")
        void constructorRejectsNullService() {
            assertThrows(NullPointerException.class, () -> new BankService(null));
        }
    }
}
