package me.medev.digitalbankingbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime operationDate;
    private double amount;
    private String type;
    private String description;

    @ManyToOne
    private BankAccount bankAccount;

    // Constructeurs
    public AccountOperation() {
    }

    public AccountOperation(double amount, String type, String description, BankAccount bankAccount) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.bankAccount = bankAccount;
        this.operationDate = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
