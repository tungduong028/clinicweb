package com.example.clinicweb.service;

import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAllPatients();
    Optional<Patient> findPatientByUsername(String username);
    Patient findByEmail(String email);
    void updateResetPasswordToken(String token, String email);
    Users getByResetPasswordToken(String token);
    void updatePassword(Users users, String newPassword);
}
