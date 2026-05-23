package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Customer;
import com.ecommerce.sb_ecom.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class CustomerServiceImpl {

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomer(){

        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(String id){
        return customerRepository.findById(id);
    }

    public Customer updateCustomer(String id, Customer updateCustomer){
    return customerRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(updateCustomer.getFirstName());
                    existing.setLastName(updateCustomer.getLastName());
                    existing.setEmail(updateCustomer.getEmail());
                    existing.setPhoneNumber(updateCustomer.getPhoneNumber());
                    return customerRepository.save(existing);
                })
            .orElseThrow(()-> new RuntimeException("Customer not found with id " + id));
    }

    public void deleteCustomer(String id){
        if (!customerRepository.existsById(id)){
            throw new RuntimeException("Customer not fount with id " +id);
        }
        customerRepository.deleteById(id);

    }
}
