package me.medev.digitalbankingbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.medev.digitalbankingbackend.dto.BankAccountDTO;
import me.medev.digitalbankingbackend.dto.CurrentAccountDTO;
import me.medev.digitalbankingbackend.dto.CustomerDTO;
import me.medev.digitalbankingbackend.dto.SavingAccountDTO;
import me.medev.digitalbankingbackend.entity.BankAccount;
import me.medev.digitalbankingbackend.entity.CurrentAccount;
import me.medev.digitalbankingbackend.entity.Customer;
import me.medev.digitalbankingbackend.entity.SavingAccount;
import me.medev.digitalbankingbackend.repository.BankAccountRepository;
import me.medev.digitalbankingbackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;

    public SavingAccountDTO createSavingAccount(double initialBalance, double interestRate, Long customerId) {
        log.info("Creating saving account for customer: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(LocalDateTime.now());
        savingAccount.setStatus("ACTIVE");
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedAccount = (SavingAccount) bankAccountRepository.save(savingAccount);
        return toSavingDTO(savedAccount);
    }

    public CurrentAccountDTO createCurrentAccount(double initialBalance, double overDraft, Long customerId) {
        log.info("Creating current account for customer: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(LocalDateTime.now());
        currentAccount.setStatus("ACTIVE");
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        CurrentAccount savedAccount = (CurrentAccount) bankAccountRepository.save(currentAccount);
        return toCurrentDTO(savedAccount);
    }

    @Transactional(readOnly = true)
    public BankAccountDTO getBankAccountById(String id) {
        log.info("Getting bank account by id: {}", id);
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
        return bankAccount.map(this::toDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<BankAccountDTO> getAllBankAccounts() {
        log.info("Getting all bank accounts");
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId) {
        log.info("Getting bank accounts for customer: {}", customerId);
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId);
        return bankAccounts.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<BankAccountDTO> getBankAccountsByStatus(String status) {
        log.info("Getting bank accounts by status: {}", status);
        List<BankAccount> bankAccounts = bankAccountRepository.findByStatus(status);
        return bankAccounts.stream().map(this::toDTO).toList();
    }

    public void debit(String accountId, double amount, String description) {
        log.info("Debiting account {} with amount: {}", accountId, amount);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (bankAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    public void credit(String accountId, double amount, String description) {
        log.info("Crediting account {} with amount: {}", accountId, amount);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    public void transfer(String fromAccountId, String toAccountId, double amount, String description) {
        log.info("Transferring {} from {} to {}", amount, fromAccountId, toAccountId);
        debit(fromAccountId, amount, "Transfer to " + toAccountId);
        credit(toAccountId, amount, "Transfer from " + fromAccountId);
    }

    public void deleteBankAccount(String id) {
        log.info("Deleting bank account with id: {}", id);
        bankAccountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return bankAccountRepository.existsById(id);
    }

    // Simple conversion methods
    private BankAccountDTO toDTO(BankAccount bankAccount) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(bankAccount.getId());
        dto.setBalance(bankAccount.getBalance());
        dto.setCreatedAt(bankAccount.getCreatedAt());
        dto.setStatus(bankAccount.getStatus());
        dto.setType(getAccountType(bankAccount));

        // Add customer information
        if (bankAccount.getCustomer() != null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(bankAccount.getCustomer().getId());
            customerDTO.setName(bankAccount.getCustomer().getName());
            customerDTO.setEmail(bankAccount.getCustomer().getEmail());
            dto.setCustomer(customerDTO);
        }

        return dto;
    }

    private SavingAccountDTO toSavingDTO(SavingAccount savingAccount) {
        SavingAccountDTO dto = new SavingAccountDTO();
        dto.setId(savingAccount.getId());
        dto.setBalance(savingAccount.getBalance());
        dto.setCreatedAt(savingAccount.getCreatedAt());
        dto.setStatus(savingAccount.getStatus());
        dto.setType("SA");
        dto.setInterestRate(savingAccount.getInterestRate());

        // Add customer information
        if (savingAccount.getCustomer() != null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(savingAccount.getCustomer().getId());
            customerDTO.setName(savingAccount.getCustomer().getName());
            customerDTO.setEmail(savingAccount.getCustomer().getEmail());
            dto.setCustomer(customerDTO);
        }

        return dto;
    }

    private CurrentAccountDTO toCurrentDTO(CurrentAccount currentAccount) {
        CurrentAccountDTO dto = new CurrentAccountDTO();
        dto.setId(currentAccount.getId());
        dto.setBalance(currentAccount.getBalance());
        dto.setCreatedAt(currentAccount.getCreatedAt());
        dto.setStatus(currentAccount.getStatus());
        dto.setType("CA");
        dto.setOverDraft(currentAccount.getOverDraft());

        // Add customer information
        if (currentAccount.getCustomer() != null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(currentAccount.getCustomer().getId());
            customerDTO.setName(currentAccount.getCustomer().getName());
            customerDTO.setEmail(currentAccount.getCustomer().getEmail());
            dto.setCustomer(customerDTO);
        }

        return dto;
    }

    private String getAccountType(BankAccount bankAccount) {
        if (bankAccount instanceof SavingAccount) {
            return "SA";
        } else if (bankAccount instanceof CurrentAccount) {
            return "CA";
        }
        return "UNKNOWN";
    }
}
