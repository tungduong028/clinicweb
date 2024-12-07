package com.example.clinicweb.repository;

import com.example.clinicweb.model.Service;
import com.example.clinicweb.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Users findByResetPasswordToken(String token);
    @Query("SELECT u FROM Users u WHERE u.role.roleId = 3")
    List<Users> findByRolePatient();
    @Query("SELECT u FROM Users u WHERE u.role.roleId = 2")
    List<Users> findByRoleDoctor();

    Page<Users> findByIsDeletedFalseAndUsernameContainingIgnoreCase(String name, Pageable pageable);
    Page<Users> findByIsDeletedFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Users s SET s.isDeleted = true WHERE s.id = :id AND s.isDeleted = false")
    int markAsDeleted(Long id);
}

