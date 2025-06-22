package me.medev.digitalbankingbackend.repository;

import me.medev.digitalbankingbackend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Recherche par nom
    List<Customer> findByName(String name);

    // Recherche par email
    Customer findByEmail(String email);

    // Recherche par nom contenant (ignorer la casse)
    List<Customer> findByNameContainingIgnoreCase(String name);
}
