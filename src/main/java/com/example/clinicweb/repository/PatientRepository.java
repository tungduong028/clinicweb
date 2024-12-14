package com.example.clinicweb.repository;

import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, CrudRepository<Patient, Long> {
    Optional<Patient> findByUser_Username(String username);

    Page<Patient> findByIsDeletedFalse(Pageable pageable);

    Page<Patient> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Patient s SET s.isDeleted = true WHERE s.patientId = :id AND s.isDeleted = false")
    int markAsDeleted(Long id);
    Patient findByEmail(String email);

}
