package me.medev.digitalbankingbackend.repository;

import me.medev.digitalbankingbackend.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    // Recherche par client
    List<BankAccount> findByCustomerId(Long customerId);

    // Recherche par statut
    List<BankAccount> findByStatus(String status);

    // Recherche par client et statut
    List<BankAccount> findByCustomerIdAndStatus(Long customerId, String status);

    // Requête personnalisée pour obtenir les comptes avec solde supérieur à un
    // montant
    @Query("SELECT ba FROM BankAccount ba WHERE ba.balance > :balance")
    List<BankAccount> findAccountsWithBalanceGreaterThan(@Param("balance") double balance);
}
