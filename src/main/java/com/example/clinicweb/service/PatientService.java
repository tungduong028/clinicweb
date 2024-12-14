package com.example.clinicweb.service;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.clinicweb.model.Users;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAllPatients();

    void savePatient(PatientDTO patient);

    Page<Patient> findAll(Pageable pageable);

    Optional<Patient> findById(Long id);

    Page<Patient> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String name, Pageable pageable);

    int markAsDeleted(Long id);

    Patient findByEmail(String email);

    void updateResetPasswordToken(String token, String email);

    Users getByResetPasswordToken(String token);

    void updatePassword(Users users, String newPassword);
}
