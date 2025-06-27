package me.medev.digitalbankingbackend.repository;

import me.medev.digitalbankingbackend.entity.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, String> {

    // Recherche par découvert
    List<CurrentAccount> findByOverDraft(double overDraft);

    // Recherche par découvert supérieur à
    List<CurrentAccount> findByOverDraftGreaterThan(double overDraft);

    // Recherche par client
    List<CurrentAccount> findByCustomerId(Long customerId);

    // Requête personnalisée pour obtenir les comptes courants avec découvert
    // autorisé
    @Query("SELECT ca FROM CurrentAccount ca WHERE ca.overDraft > 0")
    List<CurrentAccount> findAccountsWithOverdraftAllowed();

    // Requête pour obtenir les comptes avec un découvert dans une plage
    @Query("SELECT ca FROM CurrentAccount ca WHERE ca.overDraft BETWEEN :minOverdraft AND :maxOverdraft")
    List<CurrentAccount> findByOverDraftBetween(@Param("minOverdraft") double minOverdraft,
            @Param("maxOverdraft") double maxOverdraft);
}
