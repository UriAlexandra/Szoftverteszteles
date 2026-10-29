package org.example;

import java.util.Locale;
import java.util.Objects;

/**
 * Banki műveletek értesítéssel.
 *
 * <p>Az értesítési szolgáltatást konstruktoron keresztül kapja (Dependency Injection).
 * Az osztály így nem függ egy konkrét megvalósítástól, és a tesztekben
 * valódi szolgáltatás helyett mock objektum adható át neki.
 */
public class BankService {

    private final NotificationService notificationService;

    /**
     * @param notificationService az értesítéseket küldő szolgáltatás
     * @throws NullPointerException ha a szolgáltatás null
     */
    public BankService(NotificationService notificationService) {
        this.notificationService = Objects.requireNonNull(notificationService,
                "Az értesítési szolgáltatás nem lehet null!");
    }

    /**
     * Befizetést hajt végre a számlára, majd értesítést küld róla.
     *
     * <p>Ha a befizetés sikertelen, a számla kivételt dob, ezért értesítés sem megy ki.
     *
     * @param account a számla
     * @param amount  a befizetett összeg
     * @throws IllegalArgumentException ha az összeg érvénytelen (0, negatív, NaN, végtelen)
     * @throws NullPointerException     ha a számla null
     */
    public void deposit(BankAccount account, double amount) {
        Objects.requireNonNull(account, "A számla nem lehet null!");
        account.deposit(amount);
        notificationService.send(depositMessage(amount, account.getBalance()));
    }

    private static String depositMessage(double amount, double newBalance) {
        return String.format(Locale.ROOT,
                "Sikeres befizetés: %.2f Ft. Új egyenleg: %.2f Ft.", amount, newBalance);
    }
}
