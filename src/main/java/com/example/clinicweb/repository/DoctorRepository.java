package com.example.clinicweb.repository;

import com.example.clinicweb.model.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, CrudRepository<Doctor, Long> {
    Page<Doctor> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Doctor> findByIsDeletedFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Doctor s SET s.isDeleted = true WHERE s.doctorId = :id AND s.isDeleted = false")
    int markAsDeleted(Long id);
}
