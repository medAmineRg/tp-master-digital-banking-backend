package me.medev.digitalbankingbackend.controller;

import lombok.RequiredArgsConstructor;
import me.medev.digitalbankingbackend.dto.BankAccountDTO;
import me.medev.digitalbankingbackend.dto.CurrentAccountDTO;
import me.medev.digitalbankingbackend.dto.SavingAccountDTO;
import me.medev.digitalbankingbackend.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/current")
    public ResponseEntity<CurrentAccountDTO> createCurrentAccount(@RequestBody Map<String, Object> requestBody) {
        double initialBalance = Double.parseDouble(requestBody.get("balance").toString());
        double overDraft = Double.parseDouble(requestBody.get("overdraft").toString());
        Long customerId = Long.parseLong(requestBody.get("customerDTO").toString().replaceAll("[^0-9]", ""));

        CurrentAccountDTO account = bankAccountService.createCurrentAccount(initialBalance, overDraft, customerId);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PostMapping("/saving")
    public ResponseEntity<SavingAccountDTO> createSavingAccount(@RequestBody Map<String, Object> requestBody) {
        double initialBalance = Double.parseDouble(requestBody.get("balance").toString());
        double interestRate = Double.parseDouble(requestBody.get("interestRate").toString());
        Long customerId = Long.parseLong(requestBody.get("customerDTO").toString().replaceAll("[^0-9]", ""));

        SavingAccountDTO account = bankAccountService.createSavingAccount(initialBalance, interestRate, customerId);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getBankAccountById(@PathVariable String id) {
        BankAccountDTO account = bankAccountService.getBankAccountById(id);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts() {
        List<BankAccountDTO> accounts = bankAccountService.getAllBankAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BankAccountDTO>> getBankAccountsByCustomerId(@PathVariable Long customerId) {
        List<BankAccountDTO> accounts = bankAccountService.getBankAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BankAccountDTO>> getBankAccountsByStatus(@PathVariable String status) {
        List<BankAccountDTO> accounts = bankAccountService.getBankAccountsByStatus(status);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/{id}/debit")
    public ResponseEntity<Void> debit(@PathVariable String id, @RequestBody Map<String, Object> request) {
        double amount = Double.parseDouble(request.get("amount").toString());
        String description = (String) request.get("description");
        bankAccountService.debit(id, amount, description);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/credit")
    public ResponseEntity<Void> credit(@PathVariable String id, @RequestBody Map<String, Object> request) {
        double amount = Double.parseDouble(request.get("amount").toString());
        String description = (String) request.get("description");
        bankAccountService.credit(id, amount, description);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody Map<String, Object> request) {
        String fromAccountId = (String) request.get("fromAccountId");
        String toAccountId = (String) request.get("toAccountId");
        double amount = Double.parseDouble(request.get("amount").toString());
        String description = (String) request.get("description");
        bankAccountService.transfer(fromAccountId, toAccountId, amount, description);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable String id) {
        if (!bankAccountService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bankAccountService.deleteBankAccount(id);
        return ResponseEntity.noContent().build();
    }
}
