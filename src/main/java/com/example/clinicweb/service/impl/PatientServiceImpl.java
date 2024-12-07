package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public Optional<Patient> findPatientByUsername(String username) {
        return patientRepository.findByUser_Username(username);
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
