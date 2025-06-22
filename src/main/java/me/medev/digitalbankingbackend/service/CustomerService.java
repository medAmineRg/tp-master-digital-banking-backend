package me.medev.digitalbankingbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.medev.digitalbankingbackend.dto.CustomerDTO;
import me.medev.digitalbankingbackend.entity.Customer;
import me.medev.digitalbankingbackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("Creating customer with name: {}", customerDTO.getName());
        Customer customer = toEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return toDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer with id: {}", customerDTO.getId());
        Customer customer = toEntity(customerDTO);
        Customer updatedCustomer = customerRepository.save(customer);
        return toDTO(updatedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        log.info("Getting customer by id: {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(this::toDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByEmail(String email) {
        log.info("Getting customer by email: {}", email);
        Customer customer = customerRepository.findByEmail(email);
        return customer != null ? toDTO(customer) : null;
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        log.info("Getting all customers");
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomersByName(String name) {
        log.info("Searching customers by name: {}", name);
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customers.stream().map(this::toDTO).toList();
    }

    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        customerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }

    // Simple conversion methods
    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        // Don't include bankAccounts to avoid circular references
        return dto;
    }

    private Customer toEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        return customer;
    }
}
