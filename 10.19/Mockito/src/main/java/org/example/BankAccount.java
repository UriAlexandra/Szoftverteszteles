package org.example;

/**
 * Egyszerű bankszámla.
 *
 * <p>Az induló egyenleg 0. Hibás művelet esetén {@link IllegalArgumentException}
 * kivételt dob. Minden ellenőrzés a módosítás előtt történik, ezért sikertelen
 * művelet után az egyenleg változatlan marad.
 */
public class BankAccount {

    private double balance;

    /** Új számla 0 egyenleggel. */
    public BankAccount() {
        this.balance = 0.0;
    }

    /**
     * Befizetés: növeli az egyenleget.
     *
     * @param amount a befizetett összeg, 0-nál nagyobb véges szám
     * @throws IllegalArgumentException ha az összeg 0, negatív, NaN vagy végtelen
     */
    public void deposit(double amount) {
        requirePositive(amount, "A befizetés összegének 0-nál nagyobbnak kell lennie!");
        this.balance += amount;
    }

    /**
     * Pénzfelvétel: csökkenti az egyenleget.
     *
     * @param amount a felvett összeg, 0-nál nagyobb véges szám, legfeljebb az egyenleg
     * @throws IllegalArgumentException ha az összeg 0, negatív, NaN, végtelen,
     *                                  vagy nagyobb az aktuális egyenlegnél
     */
    public void withdraw(double amount) {
        requirePositive(amount, "A felvett összegnek 0-nál nagyobbnak kell lennie!");
        if (amount > this.balance) {
            throw new IllegalArgumentException("Nincs elegendő fedezet a számlán!");
        }
        this.balance -= amount;
    }

    /** Az aktuális egyenleg. */
    public double getBalance() {
        return this.balance;
    }

    /**
     * Csak 0-nál nagyobb, véges összeget enged át. A NaN és a végtelen is hibás:
     * a {@code NaN <= 0} feltétel hamis, így egy egyszerű {@code amount <= 0}
     * ellenőrzés átengedné, és az egyenleg NaN lenne.
     */
    private static void requirePositive(double amount, String message) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
