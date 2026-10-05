import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    public void setUp() {
        account = new BankAccount();
    }

    @Test
    public void testInitialBalanceIsZero() {
        // Új számla egyenlege 0.
        assertEquals(0.0, account.getBalance());
    }

    @Test
    public void testDepositIncreasesBalance() {
        // deposit(1000) után az egyenleg 1000.
        account.deposit(1000.0);
        assertEquals(1000.0, account.getBalance());
    }

    @Test
    public void testMultipleDepositsAddUp() {
        // Több befizetés helyesen összeadódik.
        account.deposit(500.0);
        account.deposit(1500.0);
        account.deposit(200.0);
        assertEquals(2200.0, account.getBalance());
    }

    @Test
    public void testWithdrawDecreasesBalance() {
        // Befizetés után a withdraw() helyesen csökkenti az egyenleget.
        account.deposit(2000.0);
        account.withdraw(500.0);
        assertEquals(1500.0, account.getBalance());
    }

    @Test
    public void testWithdrawFullBalance() {
        // A teljes egyenleg felvehető.
        account.deposit(1000.0);
        account.withdraw(1000.0);
        assertEquals(0.0, account.getBalance());
    }

    @Test
    public void testWithdrawMoreThanBalanceThrowsException() {
        // Túl nagy összeg felvétele IllegalArgumentException kivételt dob.
        account.deposit(500.0);
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(600.0);
        });
    }

    @Test
    public void testNegativeDepositThrowsException() {
        // Negatív befizetés kivételt dob.
        assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(-100.0);
        });
    }

    @Test
    public void testZeroDepositThrowsException() {
        // 0 összegű befizetés kivételt dob.
        assertThrows(IllegalArgumentException.class, () -> {
            account.deposit(0.0);
        });
    }

    @Test
    public void testNegativeWithdrawThrowsException() {
        // Negatív pénzfelvétel kivételt dob.
        account.deposit(1000.0); // Először adunk neki fedezetet
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(-200.0);
        });
    }

    @Test
    public void testZeroWithdrawThrowsException() {
        // 0 összegű pénzfelvétel kivételt dob.
        account.deposit(1000.0); // Először adunk neki fedezetet
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(0.0);
        });
    }
}