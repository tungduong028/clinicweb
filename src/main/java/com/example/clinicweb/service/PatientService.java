package com.example.clinicweb.service;

import com.example.clinicweb.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAllPatients();
    Optional<Patient> findPatientByUsername(String username);
}
