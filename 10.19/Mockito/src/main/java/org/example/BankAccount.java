package org.example;

public class BankAccount {
    private double balance;

    public BankAccount() {
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A befizetés összege nagyobb kell legyen, mint 0!");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("A felvett összeg nagyobb kell legyen, mint 0!");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Nincs elegendő fedezet a számlán!");
        }
        this.balance -= amount;
    }

    public double getBalance() {
        return this.balance;
    }
}