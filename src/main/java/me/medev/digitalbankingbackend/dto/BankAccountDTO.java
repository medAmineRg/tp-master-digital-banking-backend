package me.medev.digitalbankingbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private String id;
    private double balance;
    private LocalDateTime createdAt;
    private String status;
    private String type; // SA pour SavingAccount, CA pour CurrentAccount
    private CustomerDTO customer;
    private List<AccountOperationDTO> accountOperations;
}
