package com.ecommerce.sb_ecom.config;

import com.ecommerce.sb_ecom.model.AppRole;
import com.ecommerce.sb_ecom.model.Role;
import com.ecommerce.sb_ecom.repository.RoleRepository;
import com.ecommerce.sb_ecom.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository, SequenceGeneratorService sequenceGeneratorService) {
        return args -> {
            for (AppRole role : AppRole.values()) {
                roleRepository.findByRoleName(role)
                        .orElseGet(() -> {
                           Role newRole = new Role(role);
                            newRole.setId(
                                    sequenceGeneratorService.generateSequence(Role.SEQUENCE_NAME)
                            );
                            return roleRepository.save(newRole);
                        });
            }
        };
    }
}
