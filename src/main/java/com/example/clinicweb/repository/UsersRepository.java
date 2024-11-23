package com.example.clinicweb.repository;

import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username); // Tìm chính xác theo username

    List<Users> findByUsernameContaining(String username); // Tìm kiếm theo username chứa chuỗi
    @Query("SELECT u FROM Users u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.role.roleName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Users> findByKeyword(String keyword);
    void deleteByUserId(Long userId);
}


