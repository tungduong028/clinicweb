package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public void savePatient(Patient patient, PatientDTO patientDTO) {

        patient.setFullName(patientDTO.getFullName());
        patient.setGender(patientDTO.getGender());
        patient.setEmail(patientDTO.getEmail());
        patient.setAddress(patientDTO.getAddress());
        patient.setMedicalHistory(patientDTO.getMedicalHistory());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        patientRepository.save(patient);

//        Users user = new Users();
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        user.setPasswordHash(encoder.encode(newPassword));
//        usersRepository.save(user);

    }

    @Override
    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public int markAsDeleted(Long id){
        return patientRepository.markAsDeleted(id);
    }

    @Override
    public Page<Patient> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String name, Pageable pageable){
        return patientRepository.findByIsDeletedFalseAndFullNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        Patient patient = patientRepository.findByEmail(email);
        if (patient != null) {
            Users user = patient.getUser();
            if (user != null) {
                user.setResetPasswordToken(token);
                usersRepository.save(user);
            }
        }
    }

    @Override
    public Users getByResetPasswordToken(String token) {
        return usersRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(Users users, String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        users.setPasswordHash(encoder.encode(newPassword));
        users.setResetPasswordToken(null);
        usersRepository.save(users);
    }
}
