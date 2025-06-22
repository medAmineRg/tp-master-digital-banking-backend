package me.medev.digitalbankingbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.medev.digitalbankingbackend.dto.SavingAccountDTO;
import me.medev.digitalbankingbackend.entity.SavingAccount;
import me.medev.digitalbankingbackend.repository.SavingAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SavingAccountService {

    private final SavingAccountRepository savingAccountRepository;

    public SavingAccountDTO updateSavingAccount(SavingAccountDTO savingAccountDTO) {
        log.info("Updating saving account with id: {}", savingAccountDTO.getId());
        SavingAccount savingAccount = toEntity(savingAccountDTO);
        SavingAccount updatedAccount = savingAccountRepository.save(savingAccount);
        return toDTO(updatedAccount);
    }

    @Transactional(readOnly = true)
    public SavingAccountDTO getSavingAccountById(String id) {
        log.info("Getting saving account by id: {}", id);
        Optional<SavingAccount> savingAccount = savingAccountRepository.findById(id);
        return savingAccount.map(this::toDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SavingAccountDTO> getAllSavingAccounts() {
        log.info("Getting all saving accounts");
        List<SavingAccount> savingAccounts = savingAccountRepository.findAll();
        return savingAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SavingAccountDTO> getSavingAccountsByCustomerId(Long customerId) {
        log.info("Getting saving accounts for customer: {}", customerId);
        List<SavingAccount> savingAccounts = savingAccountRepository.findByCustomerId(customerId);
        return savingAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SavingAccountDTO> getSavingAccountsByInterestRate(double interestRate) {
        log.info("Getting saving accounts by interest rate: {}", interestRate);
        List<SavingAccount> savingAccounts = savingAccountRepository.findByInterestRate(interestRate);
        return savingAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<SavingAccountDTO> getSavingAccountsByInterestRateRange(double minRate, double maxRate) {
        log.info("Getting saving accounts with interest rate between {} and {}", minRate, maxRate);
        List<SavingAccount> savingAccounts = savingAccountRepository.findByInterestRateBetween(minRate, maxRate);
        return savingAccounts.stream().map(this::toDTO).toList();
    }

    public void updateInterestRate(String accountId, double newInterestRate) {
        log.info("Updating interest rate for account {} to {}", accountId, newInterestRate);
        SavingAccount savingAccount = savingAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Saving account not found"));

        savingAccount.setInterestRate(newInterestRate);
        savingAccountRepository.save(savingAccount);
    }

    public void deleteSavingAccount(String id) {
        log.info("Deleting saving account with id: {}", id);
        savingAccountRepository.deleteById(id);
    }

    // Simple conversion methods
    private SavingAccountDTO toDTO(SavingAccount savingAccount) {
        SavingAccountDTO dto = new SavingAccountDTO();
        dto.setId(savingAccount.getId());
        dto.setBalance(savingAccount.getBalance());
        dto.setCreatedAt(savingAccount.getCreatedAt());
        dto.setStatus(savingAccount.getStatus());
        dto.setType("SA");
        dto.setInterestRate(savingAccount.getInterestRate());
        return dto;
    }

    private SavingAccount toEntity(SavingAccountDTO dto) {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(dto.getId());
        savingAccount.setBalance(dto.getBalance());
        savingAccount.setStatus(dto.getStatus());
        savingAccount.setInterestRate(dto.getInterestRate());
        return savingAccount;
    }
}
