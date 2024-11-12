package com.example.clinicweb.repository;


import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(Users user);
    Optional<Patient> findByUser_Username(String username);
}
