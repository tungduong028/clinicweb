package com.example.clinicweb.service;

import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Users;

import java.util.List;

public interface UserService {
    Users findUserByUsername(String username);

    Users saveUser(UsersDTO userDto);

    List<Users> findAllUsers();

    void deleteUserById(Long id);

    List<Users> findByRolePatient();
}