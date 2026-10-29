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

@DisplayName("BankAccount")
class BankAccountTest {

    /** Megengedett eltérés a double típusú egyenlegek összehasonlításánál. */
    private static final double DELTA = 1e-9;

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount();
    }

    @Nested
    @DisplayName("A feladatban előírt esetek")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class RequiredCases {

        @Test
        @Order(1)
        @DisplayName("1. Új számla egyenlege 0")
        void newAccountHasZeroBalance() {
            assertEquals(0.0, account.getBalance(), DELTA);
        }

        @Test
        @Order(2)
        @DisplayName("2. deposit(1000) után az egyenleg 1000")
        void depositIncreasesBalance() {
            account.deposit(1000);
            assertEquals(1000.0, account.getBalance(), DELTA);
        }

        @Test
        @Order(3)
        @DisplayName("3. Több befizetés helyesen összeadódik")
        void multipleDepositsAddUp() {
            account.deposit(500);
            account.deposit(1500);
            account.deposit(200);
            assertEquals(2200.0, account.getBalance(), DELTA);
        }

        @Test
        @Order(4)
        @DisplayName("4. Befizetés után a withdraw() helyesen csökkenti az egyenleget")
        void withdrawDecreasesBalance() {
            account.deposit(2000);
            account.withdraw(500);
            assertEquals(1500.0, account.getBalance(), DELTA);
        }

        @Test
        @Order(5)
        @DisplayName("5. A teljes egyenleg felvehető")
        void fullBalanceCanBeWithdrawn() {
            account.deposit(1000);
            account.withdraw(1000);
            assertEquals(0.0, account.getBalance(), DELTA);
        }

        @Test
        @Order(6)
        @DisplayName("6. Túl nagy összeg felvétele IllegalArgumentException kivételt dob")
        void withdrawingMoreThanBalanceThrows() {
            account.deposit(500);
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(600));
            assertEquals(500.0, account.getBalance(), DELTA, "a sikertelen művelet nem változtathat az egyenlegen");
        }

        @Test
        @Order(7)
        @DisplayName("7. Negatív befizetés kivételt dob")
        void negativeDepositThrows() {
            account.deposit(1000);
            assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
            assertEquals(1000.0, account.getBalance(), DELTA, "a sikertelen művelet nem változtathat az egyenlegen");
        }

        @Test
        @Order(8)
        @DisplayName("8. 0 összegű befizetés kivételt dob")
        void zeroDepositThrows() {
            account.deposit(1000);
            assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
            assertEquals(1000.0, account.getBalance(), DELTA, "a sikertelen művelet nem változtathat az egyenlegen");
        }

        @Test
        @Order(9)
        @DisplayName("9. Negatív pénzfelvétel kivételt dob")
        void negativeWithdrawalThrows() {
            account.deposit(1000);
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(-200));
            assertEquals(1000.0, account.getBalance(), DELTA, "a sikertelen művelet nem változtathat az egyenlegen");
        }

        @Test
        @Order(10)
        @DisplayName("10. 0 összegű pénzfelvétel kivételt dob")
        void zeroWithdrawalThrows() {
            account.deposit(1000);
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
            assertEquals(1000.0, account.getBalance(), DELTA, "a sikertelen művelet nem változtathat az egyenlegen");
        }
    }

    @Nested
    @DisplayName("Sikertelen műveletek után az egyenleg változatlan")
    class FailedOperationsKeepBalance {

        private static final double START_BALANCE = 1000.0;

        @BeforeEach
        void depositStartingBalance() {
            account.deposit(START_BALANCE);
        }

        @ParameterizedTest(name = "befizetés: {0}")
        @ValueSource(doubles = {-1000, -100, -0.01, 0, Double.NaN,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
        @DisplayName("Hibás befizetés kivételt dob, és nem változtat az egyenlegen")
        void invalidDepositKeepsBalance(double amount) {
            assertThrows(IllegalArgumentException.class, () -> account.deposit(amount));
            assertEquals(START_BALANCE, account.getBalance(), DELTA);
        }

        @ParameterizedTest(name = "pénzfelvétel: {0}")
        @ValueSource(doubles = {-1000, -0.01, 0, 1000.01, 1001, 5000, Double.NaN,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
        @DisplayName("Hibás pénzfelvétel kivételt dob, és nem változtat az egyenlegen")
        void invalidWithdrawalKeepsBalance(double amount) {
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(amount));
            assertEquals(START_BALANCE, account.getBalance(), DELTA);
        }

        @Test
        @DisplayName("Üres számláról a legkisebb összeg sem vehető fel")
        void cannotWithdrawFromEmptyAccount() {
            BankAccount empty = new BankAccount();
            assertThrows(IllegalArgumentException.class, () -> empty.withdraw(0.01));
            assertEquals(0.0, empty.getBalance(), DELTA);
        }

        @Test
        @DisplayName("Több sikertelen művelet egymás után sem változtat az egyenlegen")
        void severalFailedOperationsInARow() {
            assertThrows(IllegalArgumentException.class, () -> account.deposit(-1));
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(2000));
            assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
            assertEquals(START_BALANCE, account.getBalance(), DELTA);
        }

        @Test
        @DisplayName("Sikertelen művelet után a számla tovább használható")
        void accountStillWorksAfterFailedOperation() {
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(5000));
            account.withdraw(400);
            account.deposit(100);
            assertEquals(700.0, account.getBalance(), DELTA);
        }
    }

    @Nested
    @DisplayName("További esetek")
    class AdditionalCases {

        @Test
        @DisplayName("Több egymás utáni pénzfelvétel helyesen csökkenti az egyenleget")
        void multipleWithdrawals() {
            account.deposit(5000);
            account.withdraw(1000);
            account.withdraw(500);
            account.withdraw(2000);
            assertEquals(1500.0, account.getBalance(), DELTA);
        }

        @Test
        @DisplayName("Váltakozó befizetések és pénzfelvételek")
        void interleavedOperations() {
            account.deposit(1000);
            account.withdraw(200);
            account.deposit(500);
            account.withdraw(300);
            assertEquals(1000.0, account.getBalance(), DELTA);
        }

        @Test
        @DisplayName("Tört összegek (fillérek) kezelése")
        void decimalAmounts() {
            account.deposit(100.50);
            account.withdraw(50.25);
            assertEquals(50.25, account.getBalance(), DELTA);

            account.deposit(0.1);
            account.deposit(0.2);
            assertEquals(50.55, account.getBalance(), DELTA, "lebegőpontos kerekítés mellett is helyes");
        }

        @Test
        @DisplayName("A kivételek hibaüzenete beszédes")
        void exceptionMessages() {
            IllegalArgumentException depositError =
                    assertThrows(IllegalArgumentException.class, () -> account.deposit(-50));
            assertEquals("A befizetés összegének 0-nál nagyobbnak kell lennie!", depositError.getMessage());

            IllegalArgumentException withdrawError =
                    assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
            assertEquals("A felvett összegnek 0-nál nagyobbnak kell lennie!", withdrawError.getMessage());

            IllegalArgumentException coverageError =
                    assertThrows(IllegalArgumentException.class, () -> account.withdraw(10));
            assertEquals("Nincs elegendő fedezet a számlán!", coverageError.getMessage());
        }
    }
}
