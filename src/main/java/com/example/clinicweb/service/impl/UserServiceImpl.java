package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Lưu người dùng mới hoặc cập nhật thông tin người dùng
    public void save(Users user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại."));

        // Kiểm tra vai trò
        String roleName = user.getRole().getRoleName();
        if ("Admin".equalsIgnoreCase(roleName)) {
            throw new RuntimeException("Không thể xóa tài khoản với vai trò Admin.");
        }

        // Xóa dữ liệu liên quan
        if ("Doctor".equalsIgnoreCase(roleName)) {
            doctorRepository.deleteByUser_UserId(userId);
        } else if ("Patient".equalsIgnoreCase(roleName)) {
            patientRepository.deleteByUser_UserId(userId);
        }

        // Xóa người dùng
        userRepository.delete(user);
    }

    public List<Users> searchUsersByKeyword(String keyword) {
        return userRepository.findByKeyword(keyword);
    }

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
        patient.setPhone_number(patientDto.getPhoneNumber());
        patient.setGender(patientDto.getGender());
        patient.setDate_of_birth(patientDto.getDateOfBirth());
        // 4. Lưu Patient vào bảng `patients`
        patientRepository.save(patient);
    }
}
