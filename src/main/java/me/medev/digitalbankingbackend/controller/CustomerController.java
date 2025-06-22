package me.medev.digitalbankingbackend.controller;

import lombok.RequiredArgsConstructor;
import me.medev.digitalbankingbackend.dto.CustomerDTO;
import me.medev.digitalbankingbackend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedCustomer = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        CustomerDTO customer = customerService.getCustomerByEmail(email);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomersByName(@RequestParam String name) {
        List<CustomerDTO> customers = customerService.searchCustomersByName(name);
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!customerService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
