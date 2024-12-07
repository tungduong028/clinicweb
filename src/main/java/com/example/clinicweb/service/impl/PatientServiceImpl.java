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
        Optional<Users> user = usersRepository.findById(patient.getUserId());
        user.ifPresent(patient1::setUser);
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

    public int markAsDeleted(Long id){return patientRepository.markAsDeleted(id);}

    public Page<Patient> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String name, Pageable pageable){ return patientRepository.findByIsDeletedFalseAndFullNameContainingIgnoreCase(name, pageable);}
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
