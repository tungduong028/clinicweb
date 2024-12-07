package com.example.clinicweb.service;

import com.example.clinicweb.dto.ServiceDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Service;
import com.example.clinicweb.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Users findUserByUsername(String username);

    Users saveUser(UsersDTO userDto);

    List<Users> findAllUsers();

    void deleteUserById(Long id);

    List<Users> findByRolePatient();

    List<Users> findByRoleDoctor();

    Page<Users> findByIsDeletedFalseAndUsernameContainingIgnoreCase(String name, Pageable pageable);
    Page<Users> findByIsDeletedFalse(Pageable pageable);
    int markAsDeleted(Long id);

    Page<Users> findAll(Pageable pageable);

    Optional<Users> findById(Long id);
}