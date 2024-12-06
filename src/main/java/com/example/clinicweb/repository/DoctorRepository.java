package com.example.clinicweb.repository;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser_UserId(Long userId);

    List<Doctor> findByIsDeletedFalse();

    List<Doctor> findByFullNameContainingIgnoreCase(String fullName);

    Optional<Doctor> findByFullName(String fullName);


    // Tìm kiếm bác sĩ theo tên và phân trang
    Page<Doctor> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    // Phân trang danh sách bác sĩ
    Page<Doctor> findAll(Pageable pageable);


    Page<Doctor> findByIsDeletedFalse(Pageable pageable);

    Page<Doctor> findByFullNameContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);




}