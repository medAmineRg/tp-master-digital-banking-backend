package me.medev.digitalbankingbackend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {

    private double overDraft;

    // Constructeurs
    public CurrentAccount() {
    }

    public CurrentAccount(String id, double balance, String status, Customer customer, double overDraft) {
        super(id, balance, status, customer);
        this.overDraft = overDraft;
    }

    // Getters et Setters
    public double getOverDraft() {
        return overDraft;
    }

    public void setOverDraft(double overDraft) {
        this.overDraft = overDraft;
    }
}
