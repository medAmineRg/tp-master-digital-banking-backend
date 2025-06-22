package me.medev.digitalbankingbackend.repository;

import me.medev.digitalbankingbackend.entity.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    // Recherche par compte bancaire
    List<AccountOperation> findByBankAccountId(String bankAccountId);

    // Recherche par compte bancaire avec pagination
    Page<AccountOperation> findByBankAccountId(String bankAccountId, Pageable pageable);

    // Recherche par type d'opération
    List<AccountOperation> findByType(String type);

    // Recherche par compte et type
    List<AccountOperation> findByBankAccountIdAndType(String bankAccountId, String type);

    // Recherche par date d'opération entre deux dates
    List<AccountOperation> findByOperationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Recherche par compte et date d'opération entre deux dates
    List<AccountOperation> findByBankAccountIdAndOperationDateBetween(String bankAccountId, LocalDateTime startDate,
            LocalDateTime endDate);

    // Requête personnalisée pour obtenir les opérations triées par date
    // décroissante
    @Query("SELECT ao FROM AccountOperation ao WHERE ao.bankAccount.id = :accountId ORDER BY ao.operationDate DESC")
    List<AccountOperation> findByAccountIdOrderByDateDesc(@Param("accountId") String accountId);

    // Requête pour obtenir les opérations par montant supérieur à
    @Query("SELECT ao FROM AccountOperation ao WHERE ao.amount > :amount")
    List<AccountOperation> findOperationsWithAmountGreaterThan(@Param("amount") double amount);
}
