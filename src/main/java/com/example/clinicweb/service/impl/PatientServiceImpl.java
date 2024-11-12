package com.example.clinicweb.service.impl;

import com.example.clinicweb.model.Patient;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> findPatientByUsername(String username) {
        return patientRepository.findByUser_Username(username);
    }
}
