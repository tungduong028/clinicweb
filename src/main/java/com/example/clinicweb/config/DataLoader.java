package com.example.clinicweb.config;

import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Admin;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.AdminRepository;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, UsersRepository usersRepository, AdminRepository adminRepository) {
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
            if (usersRepository.findById(1L).isEmpty()) {
                Users user = new Users();
                user.setUsername("admin001");
                user.setPasswordHash(passwordEncoder.encode("admin001"));
                user.setRole(roleRepository.findById(1L).orElse(null));
                Users savedUser = usersRepository.save(user);
                Admin admin = new Admin();
                admin.setUser(savedUser);
                adminRepository.save(admin);
            }
            if (usersRepository.findById(2L).isEmpty()) {
                Users user = new Users();
                user.setUsername("admin002");
                user.setPasswordHash(passwordEncoder.encode("admin002"));
                user.setRole(roleRepository.findById(1L).orElse(null));
                Users savedUser = usersRepository.save(user);
                Admin admin = new Admin();
                admin.setUser(savedUser);
                adminRepository.save(admin);
            }
            if (usersRepository.findById(3L).isEmpty()) {
                Users user = new Users();
                user.setUsername("admin003");
                user.setPasswordHash(passwordEncoder.encode("admin003"));
                user.setRole(roleRepository.findById(1L).orElse(null));
                Users savedUser = usersRepository.save(user);
                Admin admin = new Admin();
                admin.setUser(savedUser);
                adminRepository.save(admin);
            }
        };
    }
}

