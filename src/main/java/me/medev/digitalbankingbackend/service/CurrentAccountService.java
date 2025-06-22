package me.medev.digitalbankingbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.medev.digitalbankingbackend.dto.CurrentAccountDTO;
import me.medev.digitalbankingbackend.entity.CurrentAccount;
import me.medev.digitalbankingbackend.repository.CurrentAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CurrentAccountService {

    private final CurrentAccountRepository currentAccountRepository;

    public CurrentAccountDTO updateCurrentAccount(CurrentAccountDTO currentAccountDTO) {
        log.info("Updating current account with id: {}", currentAccountDTO.getId());
        CurrentAccount currentAccount = toEntity(currentAccountDTO);
        CurrentAccount updatedAccount = currentAccountRepository.save(currentAccount);
        return toDTO(updatedAccount);
    }

    @Transactional(readOnly = true)
    public CurrentAccountDTO getCurrentAccountById(String id) {
        log.info("Getting current account by id: {}", id);
        Optional<CurrentAccount> currentAccount = currentAccountRepository.findById(id);
        return currentAccount.map(this::toDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<CurrentAccountDTO> getAllCurrentAccounts() {
        log.info("Getting all current accounts");
        List<CurrentAccount> currentAccounts = currentAccountRepository.findAll();
        return currentAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CurrentAccountDTO> getCurrentAccountsByCustomerId(Long customerId) {
        log.info("Getting current accounts for customer: {}", customerId);
        List<CurrentAccount> currentAccounts = currentAccountRepository.findByCustomerId(customerId);
        return currentAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CurrentAccountDTO> getCurrentAccountsByOverDraft(double overDraft) {
        log.info("Getting current accounts by overdraft: {}", overDraft);
        List<CurrentAccount> currentAccounts = currentAccountRepository.findByOverDraft(overDraft);
        return currentAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CurrentAccountDTO> getCurrentAccountsWithOverdraftAllowed() {
        log.info("Getting current accounts with overdraft allowed");
        List<CurrentAccount> currentAccounts = currentAccountRepository.findAccountsWithOverdraftAllowed();
        return currentAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CurrentAccountDTO> getCurrentAccountsByOverDraftRange(double minOverdraft, double maxOverdraft) {
        log.info("Getting current accounts with overdraft between {} and {}", minOverdraft, maxOverdraft);
        List<CurrentAccount> currentAccounts = currentAccountRepository.findByOverDraftBetween(minOverdraft,
                maxOverdraft);
        return currentAccounts.stream().map(this::toDTO).toList();
    }

    public void updateOverDraft(String accountId, double newOverDraft) {
        log.info("Updating overdraft for account {} to {}", accountId, newOverDraft);
        CurrentAccount currentAccount = currentAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Current account not found"));

        currentAccount.setOverDraft(newOverDraft);
        currentAccountRepository.save(currentAccount);
    }

    public boolean canDebit(String accountId, double amount) {
        CurrentAccount currentAccount = currentAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Current account not found"));

        return (currentAccount.getBalance() + currentAccount.getOverDraft()) >= amount;
    }

    public void deleteCurrentAccount(String id) {
        log.info("Deleting current account with id: {}", id);
        currentAccountRepository.deleteById(id);
    }

    // Simple conversion methods
    private CurrentAccountDTO toDTO(CurrentAccount currentAccount) {
        CurrentAccountDTO dto = new CurrentAccountDTO();
        dto.setId(currentAccount.getId());
        dto.setBalance(currentAccount.getBalance());
        dto.setCreatedAt(currentAccount.getCreatedAt());
        dto.setStatus(currentAccount.getStatus());
        dto.setType("CA");
        dto.setOverDraft(currentAccount.getOverDraft());
        return dto;
    }

    private CurrentAccount toEntity(CurrentAccountDTO dto) {
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(dto.getId());
        currentAccount.setBalance(dto.getBalance());
        currentAccount.setStatus(dto.getStatus());
        currentAccount.setOverDraft(dto.getOverDraft());
        return currentAccount;
    }
}
