package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.model.Customer;
import com.ecommerce.sb_ecom.service.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private final CustomerServiceImpl customerServiceImpl ;

    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer){
        return ResponseEntity.ok(customerServiceImpl.createCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(customerServiceImpl.getAllCustomer());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") String id){
        return customerServiceImpl.getCustomerById(id)
                .map(ResponseEntity :: ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") String id,
                                                   @RequestBody Customer updateCustomer){
        try {
            return ResponseEntity.ok(customerServiceImpl.updateCustomer(id, updateCustomer));
        } catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") String id){
        try {
            customerServiceImpl.deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted successfully !!");
        } catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}
