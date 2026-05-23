package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

}
