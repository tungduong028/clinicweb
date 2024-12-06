package com.example.clinicweb.repository;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username); // Tìm chính xác theo username

    // Tìm kiếm theo username (không phân biệt chữ hoa chữ thường)

    Optional<Users> findByRole_RoleName(String roleName);
    List<Users> findByRole(Role role); // Tìm người dùng theo Role
    boolean existsByUsername(String username);
    // Tìm kiếm người dùng theo tên người dùng
//    Users findByUsername(String username);


    // Tìm kiếm người dùng theo vai trò
    Page<Users> findByRole_RoleName(String roleName, Pageable pageable);

    Page<Users> findByIsDeletedFalse(Pageable pageable);
    List<Users> findByIsDeletedFalse();
//    List<Users> findByIsDeletedFalse();

    Page<Users> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
//    // Kiểm tra sự tồn tại của người dùng theo tên người dùng (username)
//    boolean existsByUsername(String username);
}


