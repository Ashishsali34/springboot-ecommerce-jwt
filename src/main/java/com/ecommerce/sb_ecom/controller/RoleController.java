package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.model.Role;
import com.ecommerce.sb_ecom.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @GetMapping
    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
