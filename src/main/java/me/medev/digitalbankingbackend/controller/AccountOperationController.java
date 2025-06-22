package me.medev.digitalbankingbackend.controller;

import lombok.RequiredArgsConstructor;
import me.medev.digitalbankingbackend.dto.AccountOperationDTO;
import me.medev.digitalbankingbackend.service.AccountOperationService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountOperationController {

    private final AccountOperationService accountOperationService;

    @PostMapping
    public ResponseEntity<AccountOperationDTO> createAccountOperation(@RequestBody AccountOperationDTO operationDTO) {
        AccountOperationDTO savedOperation = accountOperationService.createAccountOperation(operationDTO);
        return new ResponseEntity<>(savedOperation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountOperationDTO> getAccountOperationById(@PathVariable Long id) {
        AccountOperationDTO operation = accountOperationService.getAccountOperationById(id);
        return operation != null ? ResponseEntity.ok(operation) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AccountOperationDTO>> getAllAccountOperations() {
        List<AccountOperationDTO> operations = accountOperationService.getAllAccountOperations();
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<AccountOperationDTO>> getAccountOperationsByBankAccountId(
            @PathVariable String accountId) {
        List<AccountOperationDTO> operations = accountOperationService.getAccountOperationsByBankAccountId(accountId);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/account/{accountId}/paged")
    public ResponseEntity<Page<AccountOperationDTO>> getAccountOperationsByBankAccountIdPaged(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AccountOperationDTO> operations = accountOperationService.getAccountOperationsByBankAccountId(accountId,
                page, size);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AccountOperationDTO>> getAccountOperationsByType(@PathVariable String type) {
        List<AccountOperationDTO> operations = accountOperationService.getAccountOperationsByType(type);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/account/{accountId}/type/{type}")
    public ResponseEntity<List<AccountOperationDTO>> getAccountOperationsByBankAccountIdAndType(
            @PathVariable String accountId,
            @PathVariable String type) {
        List<AccountOperationDTO> operations = accountOperationService
                .getAccountOperationsByBankAccountIdAndType(accountId, type);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AccountOperationDTO>> getAccountOperationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AccountOperationDTO> operations = accountOperationService.getAccountOperationsByDateRange(startDate,
                endDate);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<List<AccountOperationDTO>> getAccountOperationsByBankAccountIdAndDateRange(
            @PathVariable String accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AccountOperationDTO> operations = accountOperationService.getAccountOperationsByBankAccountIdAndDateRange(
                accountId, startDate, endDate);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/account/{accountId}/ordered")
    public ResponseEntity<List<AccountOperationDTO>> getAccountOperationsByAccountIdOrderByDateDesc(
            @PathVariable String accountId) {
        List<AccountOperationDTO> operations = accountOperationService
                .getAccountOperationsByAccountIdOrderByDateDesc(accountId);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/amount-greater-than/{amount}")
    public ResponseEntity<List<AccountOperationDTO>> getOperationsWithAmountGreaterThan(
            @PathVariable double amount) {
        List<AccountOperationDTO> operations = accountOperationService.getOperationsWithAmountGreaterThan(amount);
        return ResponseEntity.ok(operations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountOperation(@PathVariable Long id) {
        if (!accountOperationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        accountOperationService.deleteAccountOperation(id);
        return ResponseEntity.noContent().build();
    }
}
