package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount();
    }

    // 1. Új számla egyenlege 0.
    @Test
    void testNewAccountBalanceIsZero() {
        assertEquals(0.0, account.getBalance());
    }

    // 2. deposit(1000) után az egyenleg 1000.
    @Test
    void testDepositIncreasesBalance() {
        account.deposit(1000.0);
        assertEquals(1000.0, account.getBalance());
    }

    // 3. Több befizetés helyesen összeadódik.
    @Test
    void testMultipleDepositsAddUpCorrectly() {
        account.deposit(500.0);
        account.deposit(300.0);
        account.deposit(200.0);
        assertEquals(1000.0, account.getBalance());
    }

    // 4. Befizetés után a withdraw() helyesen csökkenti az egyenleget.
    @Test
    void testWithdrawDecreasesBalance() {
        account.deposit(1000.0);
        account.withdraw(400.0);
        assertEquals(600.0, account.getBalance());
    }

    // 5. A teljes egyenleg felvehető.
    @Test
    void testFullBalanceCanBeWithdrawn() {
        account.deposit(1000.0);
        account.withdraw(1000.0);
        assertEquals(0.0, account.getBalance());
    }

    // 6. Túl nagy összeg felvétele IllegalArgumentException kivételt dob.
    @Test
    void testWithdrawMoreThanBalanceThrowsException() {
        account.deposit(500.0);
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(600.0);
        });
    }

    // 7. Negatív befizetés kivételt dob.
    @Test
    void testNegativeDepositThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(-100.0);
        });
    }

    // 8. 0 összegű befizetés kivételt dob.
    @Test
    void testZeroDepositThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(0.0);
        });
    }

    // 9. Negatív pénzfelvétel kivételt dob.
    @Test
    void testNegativeWithdrawalThrowsException() {
        account.deposit(500.0);
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(-100.0);
        });
    }

    // 10. 0 összegű pénzfelvétel kivételt dob.
    @Test
    void testZeroWithdrawalThrowsException() {
        account.deposit(500.0);
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(0.0);
        });
    }
}