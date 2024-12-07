package com.example.clinicweb.service;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAllPatients();
    Optional<Patient> findPatientByUsername(String username);
    List<Patient> getAllPatient(Integer pageNo, Integer pageSize, String sortBy);
    void savePatient(PatientDTO patient);
    Page<Patient> findByFullNameLikeIgnoreCase(String fullname, Pageable pageable);
    Page<Patient> findByPhoneNumberLike(String phoneNumber, Pageable pageable);
    Page<Patient> findByEmailLike(String email, Pageable pageable);
    Page<Patient> findAll(Pageable pageable);
    void deleteById(Long id);
    Optional<Patient> findById(Long id);
    Page<Patient> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String name, Pageable pageable);
    int markAsDeleted(Long id);
}
