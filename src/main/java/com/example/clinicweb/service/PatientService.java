package com.example.clinicweb.service;

import com.example.clinicweb.model.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> findAllPatients();
}
