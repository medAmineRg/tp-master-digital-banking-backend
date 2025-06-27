package me.medev.digitalbankingbackend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {

    private double interestRate;

    // Constructeurs
    public SavingAccount() {
    }

    public SavingAccount(String id, double balance, String status, Customer customer, double interestRate) {
        super(id, balance, status, customer);
        this.interestRate = interestRate;
    }

    // Getters et Setters
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
