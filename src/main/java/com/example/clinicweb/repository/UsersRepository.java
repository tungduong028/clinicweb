package com.example.clinicweb.repository;

import com.example.clinicweb.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    @Query("SELECT u FROM Users u WHERE u.role.roleId = 3")
    List<Users> findByRolePatient();
}

