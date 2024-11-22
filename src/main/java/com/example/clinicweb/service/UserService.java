package com.example.clinicweb.service;

import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Users;

import java.util.List;

public interface UserService {
    List<Users> getUsers();
    Users findById(Long id);
    void save(Users user);
    void deleteUser(Long id);
    List<Users> searchUsersByKeyword(String keyword);
}