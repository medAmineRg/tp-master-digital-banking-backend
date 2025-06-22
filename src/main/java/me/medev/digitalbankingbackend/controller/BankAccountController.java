package me.medev.digitalbankingbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Bank Accounts", description = "Bank Account Management API")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @PostMapping("/current")
    @Operation(summary = "Create a new current account", description = "Creates a new current account with the specified balance, overdraft, and associates it with a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Current account created successfully", content = @Content(schema = @Schema(implementation = CurrentAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CurrentAccountDTO> createCurrentAccount(@RequestBody Map<String, Object> requestBody) {
        double initialBalance = Double.parseDouble(requestBody.get("balance").toString());
        double overDraft = Double.parseDouble(requestBody.get("overdraft").toString());
        Long customerId = Long.parseLong(requestBody.get("customerDTO").toString().replaceAll("[^0-9]", ""));

        CurrentAccountDTO account = bankAccountService.createCurrentAccount(initialBalance, overDraft, customerId);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PostMapping("/saving")
    @Operation(summary = "Create a new saving account", description = "Creates a new saving account with the specified balance, interest rate, and associates it with a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saving account created successfully", content = @Content(schema = @Schema(implementation = SavingAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<SavingAccountDTO> createSavingAccount(@RequestBody Map<String, Object> requestBody) {
        double initialBalance = Double.parseDouble(requestBody.get("balance").toString());
        double interestRate = Double.parseDouble(requestBody.get("interestRate").toString());
        Long customerId = Long.parseLong(requestBody.get("customerDTO").toString().replaceAll("[^0-9]", ""));

        SavingAccountDTO account = bankAccountService.createSavingAccount(initialBalance, interestRate, customerId);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bank account by ID", description = "Retrieves a bank account's details by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank account found", content = @Content(schema = @Schema(implementation = BankAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bank account not found")
    })
    public ResponseEntity<BankAccountDTO> getBankAccountById(
            @Parameter(description = "Bank account ID") @PathVariable String id) {
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
