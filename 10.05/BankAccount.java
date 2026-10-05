public class BankAccount {
    private double balance;

    // Konstruktor: a számla induló egyenlege 0
    public BankAccount() {
        this.balance = 0.0;
    }

    // Befizetés
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A befizetés összege szigorúan pozitív kell, hogy legyen!");
        }
        this.balance += amount;
    }

    // Pénzfelvétel
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A felvett összeg szigorúan pozitív kell, hogy legyen!");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Nincs elegendő fedezet a számlán!");
        }
        this.balance -= amount;
    }

    // Egyenleg lekérdezése
    public double getBalance() {
        return this.balance;
    }
}