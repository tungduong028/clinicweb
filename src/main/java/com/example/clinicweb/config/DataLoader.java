package com.example.clinicweb.config;

import com.example.clinicweb.model.Role;
import com.example.clinicweb.dto.RoleDTO;
import com.example.clinicweb.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findById(1L).isEmpty()) {
                Role role = new Role();
                role.setRoleName("ROLE_ADMIN");
                roleRepository.save(role);
            }
            if (roleRepository.findById(2L).isEmpty()) {
                Role role = new Role();
                role.setRoleName("ROLE_DOCTOR");
                roleRepository.save(role);
            }
            if (roleRepository.findById(3L).isEmpty()) {
                Role role = new Role();
                role.setRoleName("ROLE_PATIENT");
                roleRepository.save(role);
            }
        };
    }
}

