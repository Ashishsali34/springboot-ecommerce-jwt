package com.ecommerce.sb_ecom.repository;

import com.ecommerce.sb_ecom.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ecommerce.sb_ecom.model.AppRole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByRoleName(AppRole roleName);
}
