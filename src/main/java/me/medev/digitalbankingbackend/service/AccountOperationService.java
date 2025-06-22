package me.medev.digitalbankingbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.medev.digitalbankingbackend.dto.AccountOperationDTO;
import me.medev.digitalbankingbackend.entity.AccountOperation;
import me.medev.digitalbankingbackend.entity.BankAccount;
import me.medev.digitalbankingbackend.repository.AccountOperationRepository;
import me.medev.digitalbankingbackend.repository.BankAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountOperationService {

    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountRepository bankAccountRepository;

    public AccountOperationDTO createAccountOperation(AccountOperationDTO accountOperationDTO) {
        log.info("Creating account operation for account: {}", accountOperationDTO.getBankAccountId());

        BankAccount bankAccount = bankAccountRepository.findById(accountOperationDTO.getBankAccountId())
                .orElseThrow(() -> new RuntimeException("Bank account not found"));

        AccountOperation accountOperation = toEntity(accountOperationDTO);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(LocalDateTime.now());

        AccountOperation savedOperation = accountOperationRepository.save(accountOperation);
        return toDTO(savedOperation);
    }

    @Transactional(readOnly = true)
    public AccountOperationDTO getAccountOperationById(Long id) {
        log.info("Getting account operation by id: {}", id);
        Optional<AccountOperation> accountOperation = accountOperationRepository.findById(id);
        return accountOperation.map(this::toDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getAllAccountOperations() {
        log.info("Getting all account operations");
        List<AccountOperation> accountOperations = accountOperationRepository.findAll();
        return accountOperations.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getAccountOperationsByBankAccountId(String bankAccountId) {
        log.info("Getting account operations for bank account: {}", bankAccountId);
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(bankAccountId);
        return accountOperations.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<AccountOperationDTO> getAccountOperationsByBankAccountId(String bankAccountId, int page, int size) {
        log.info("Getting account operations for bank account: {} (page: {}, size: {})", bankAccountId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(bankAccountId,
                pageable);
        return accountOperations.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getAccountOperationsByType(String type) {
        log.info("Getting account operations by type: {}", type);
        List<AccountOperation> accountOperations = accountOperationRepository.findByType(type);
        return accountOperations.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getAccountOperationsByBankAccountIdAndType(String bankAccountId, String type) {
        log.info("Getting account operations for bank account: {} and type: {}", bankAccountId, type);
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdAndType(bankAccountId,
                type);
        return accountOperations.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getAccountOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting account operations between {} and {}", startDate, endDate);
        List<AccountOperation> accountOperations = accountOperationRepository.findByOperationDateBetween(startDate,
                endDate);
        return accountOperations.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getAccountOperationsByBankAccountIdAndDateRange(String bankAccountId,
            LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting account operations for bank account: {} between {} and {}", bankAccountId, startDate,
                endDate);
        List<AccountOperation> accountOperations = accountOperationRepository
                .findByBankAccountIdAndOperationDateBetween(bankAccountId, startDate, endDate);
        return accountOperations.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getAccountOperationsByAccountIdOrderByDateDesc(String accountId) {
        log.info("Getting account operations for account: {} ordered by date desc", accountId);
        List<AccountOperation> accountOperations = accountOperationRepository.findByAccountIdOrderByDateDesc(accountId);
        return accountOperations.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AccountOperationDTO> getOperationsWithAmountGreaterThan(double amount) {
        log.info("Getting operations with amount greater than: {}", amount);
        List<AccountOperation> accountOperations = accountOperationRepository
                .findOperationsWithAmountGreaterThan(amount);
        return accountOperations.stream().map(this::toDTO).toList();
    }

    public void deleteAccountOperation(Long id) {
        log.info("Deleting account operation with id: {}", id);
        accountOperationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return accountOperationRepository.existsById(id);
    }

    // Simple conversion methods
    private AccountOperationDTO toDTO(AccountOperation operation) {
        AccountOperationDTO dto = new AccountOperationDTO();
        dto.setId(operation.getId());
        dto.setOperationDate(operation.getOperationDate());
        dto.setAmount(operation.getAmount());
        dto.setType(operation.getType());
        dto.setDescription(operation.getDescription());
        dto.setBankAccountId(operation.getBankAccount() != null ? operation.getBankAccount().getId() : null);
        return dto;
    }

    private AccountOperation toEntity(AccountOperationDTO dto) {
        AccountOperation operation = new AccountOperation();
        operation.setId(dto.getId());
        operation.setOperationDate(dto.getOperationDate());
        operation.setAmount(dto.getAmount());
        operation.setType(dto.getType());
        operation.setDescription(dto.getDescription());
        // BankAccount will be set in the service method
        return operation;
    }
}
