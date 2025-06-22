package me.medev.digitalbankingbackend.repository;

import me.medev.digitalbankingbackend.entity.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingAccountRepository extends JpaRepository<SavingAccount, String> {

    // Recherche par taux d'intérêt
    List<SavingAccount> findByInterestRate(double interestRate);

    // Recherche par taux d'intérêt supérieur à
    List<SavingAccount> findByInterestRateGreaterThan(double interestRate);

    // Recherche par client
    List<SavingAccount> findByCustomerId(Long customerId);

    // Requête personnalisée pour obtenir les comptes épargne avec un taux d'intérêt
    // dans une plage
    @Query("SELECT sa FROM SavingAccount sa WHERE sa.interestRate BETWEEN :minRate AND :maxRate")
    List<SavingAccount> findByInterestRateBetween(@Param("minRate") double minRate, @Param("maxRate") double maxRate);
}
