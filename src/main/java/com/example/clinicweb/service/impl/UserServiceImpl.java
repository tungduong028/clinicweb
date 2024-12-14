package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users saveUser(UsersDTO userDto) {
        Role userRole = roleRepository.findByroleName(userDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Users user = new Users();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setPasswordHash(userDto.getPassword());
        user.setRole(userRole);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void register(UsersDTO userDto, PatientDTO patientDto) {
        // 1. Lấy role PATIENT từ RoleRepository
        Role patientRole = roleRepository.findByroleName("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("Role PATIENT not found"));

        // 2. Tạo đối tượng Users và lưu vào bảng `users`
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword())); // Mã hóa mật khẩu
        user.setRole(patientRole);

        Users savedUser = userRepository.save(user);

        // 3. Tạo đối tượng Patient và liên kết với user đã tạo
        Patient patient = new Patient();
        patient.setAddress(patientDto.getAddress());
        patient.setMedicalHistory(patientDto.getMedicalHistory());

        patient.setUser(savedUser);

        patient.setFullName(patientDto.getFullName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setGender(patientDto.getGender());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        // 4. Lưu Patient vào bảng `patients`
        patientRepository.save(patient);
    }

    @Override
    public List<Users> findByRolePatient(){
        return userRepository.findByRolePatient();
    }

    @Override
    public List<Users> findByRoleDoctor(){
        return userRepository.findByRoleDoctor();
    }

    @Override
    public Page<Users> findByIsDeletedFalseAndUsernameContainingIgnoreCase(String name, Pageable pageable){
        return userRepository.findByIsDeletedFalseAndUsernameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Users> findByIsDeletedFalse(Pageable pageable){
        return userRepository.findByIsDeletedFalse(pageable);
    }

    @Override
    public int markAsDeleted(Long id){
        return userRepository.markAsDeleted(id);
    }

    @Override
    public Page<Users> findAll(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<Users> findById(Long id){
        return userRepository.findById(id);
    }
}
