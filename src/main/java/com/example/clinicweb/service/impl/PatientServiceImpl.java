package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<Patient> getAllPatient(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Patient> pagedResult = patientRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Patient>();
        }
    }

    public void savePatient(PatientDTO patient) {
        Patient patient1 = new Patient();
        patient1.setFullName(patient.getFullName());
        patient1.setPatientId(patient.getPatientId());
        patient1.setGender(patient.getGender());
        patient1.setEmail(patient.getEmail());
        patient1.setAddress(patient.getAddress());
        patient1.setMedicalHistory(patient.getMedicalHistory());
        patient1.setDateOfBirth(patient.getDateOfBirth());
        patient1.setPhoneNumber(patient.getPhoneNumber());
        Optional<Patient> user = patientRepository.findById(patient.getUserId());
        user.ifPresent(value -> patient1.setUser(value.getUser()));
        patientRepository.save(patient1);
    }

    public Page<Patient> findByFullNameLikeIgnoreCase(String name, Pageable pageable) {
        return  patientRepository.findByFullNameLikeIgnoreCase(name, pageable);
    }

    public Page<Patient> findByEmailLike(String email, Pageable pageable) {
        return patientRepository.findByEmailLike(email, pageable);
    }

    public Page<Patient> findByPhoneNumberLike(String phoneNumber, Pageable pageable) {
        return patientRepository.findByPhoneNumberLike(phoneNumber, pageable);
    }

    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }
}
