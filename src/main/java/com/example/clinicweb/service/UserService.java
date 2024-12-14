package com.example.clinicweb.service;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Users saveUser(UsersDTO userDto);

    List<Users> findByRolePatient();

    List<Users> findByRoleDoctor();

    Page<Users> findByIsDeletedFalseAndUsernameContainingIgnoreCase(String name, Pageable pageable);

    Page<Users> findByIsDeletedFalse(Pageable pageable);

    int markAsDeleted(Long id);

    Page<Users> findAll(Pageable pageable);

    Optional<Users> findById(Long id);

    void register(UsersDTO usersDTO, PatientDTO patientDTO);
}