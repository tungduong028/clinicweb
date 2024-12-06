package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<UsersDTO> findAllActiveUsers(Pageable pageable) {
        // Gọi repository để lấy danh sách người dùng chưa bị xóa
        Page<Users> activeUsers = userRepository.findByIsDeletedFalse(pageable);

        // Chuyển đổi Page<Users> thành Page<UsersDTO>
        return activeUsers.map(this::convertToDTO);
    }


    @Transactional
    public void addUser(UsersDTO userDto, DoctorDTO doctorDto, PatientDTO patientDto) {
        // 1. Lấy role từ RoleRepository
        System.out.println("Role name received: " + userDto.getRoleName());

        if (userDto.getRoleName() == null || userDto.getRoleName().isEmpty()) {
            throw new RuntimeException("Role name is missing or empty in the request for user: " + userDto.getUsername());
        }


        // Tìm role từ database
        Role role = roleRepository.findByRoleName(userDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role with name " + userDto.getRoleName() + " not found"));

        // 2. Tạo đối tượng Users và lưu vào bảng `users`
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword())); // Mã hóa mật khẩu
        user.setRole(role);

        Users savedUser = userRepository.save(user);

        // 3. Kiểm tra role để thêm bác sĩ hoặc bệnh nhân
        if ("ROLE_DOCTOR".equals(role.getRoleName())) {
            // Nếu là bác sĩ, tạo đối tượng Doctor
            Doctor doctor = new Doctor();
            doctor.setFullName(doctorDto.getFullName());
            doctor.setEmail(doctorDto.getEmail());
            doctor.setPhoneNumber(doctorDto.getPhoneNumber());
            doctor.setGender(doctorDto.getGender());
            doctor.setDateOfBirth(doctorDto.getDateOfBirth());
            doctor.setSpecialization(doctorDto.getSpecialization());
            doctor.setExperienceYears(doctorDto.getExperienceYears());
            doctor.setRoomId(doctorDto.getRoomId());
            doctor.setUser(savedUser); // Liên kết với User

            doctorRepository.save(doctor);
        } else if ("ROLE_PATIENT".equals(role.getRoleName())) {
            // Nếu là bệnh nhân, tạo đối tượng Patient
            Patient patient = new Patient();
            patient.setFullName(patientDto.getFullName());
            patient.setEmail(patientDto.getEmail());
            patient.setPhoneNumber(patientDto.getPhoneNumber());
            patient.setGender(patientDto.getGender());
            patient.setDateOfBirth(patientDto.getDateOfBirth());
            patient.setAddress(patientDto.getAddress());
            patient.setMedicalHistory(patientDto.getMedicalHistory());
            patient.setUser(savedUser); // Liên kết với User

            patientRepository.save(patient);
        }
    }



//    @Override
//    public void deleteUser(Long userId) {
//        Users user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));
//        userRepository.delete(user);
//    }

    @Override
    public Page<UsersDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public Page<UsersDTO> searchUsersByNameWithPagination(String name, Pageable pageable) {
        Page<Users> userPage = userRepository.findByUsernameContainingIgnoreCase(name, pageable);
        return userPage.map(this::convertToDTO);
    }

//    @Override
//    public Page<UsersDTO> findAllActiveUsers(Pageable pageable) {
//        // Lấy danh sách bác sĩ chưa bị xóa từ DB
//        Page<Users> activeUsers = userRepository.findByIsDeletedFalse(pageable);
//
//        // Chuyển đổi sang DTO trước khi trả về
//        return activeUsers.map(this::convertToDTO);
//    }

    @Override
    public List<UsersDTO> findAllUsers() {
        List<Users> users = userRepository.findByIsDeletedFalse();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UsersDTO convertToDTO(Users user) {
        UsersDTO dto = new UsersDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setRoleName(user.getRole().getRoleName());
        return dto;
    }

    @Override
    public Optional<UsersDTO> findById(Long userId) {
        Optional<Users> user = userRepository.findById(userId);
        return user.map(this::convertToDTO); // Chuyển đổi Doctor entity sang DTO
    }

    // Xóa bác sĩ
    @Override
    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + userId));

        user.setDeleted(true); // Đánh dấu là đã xóa
        userRepository.save(user); // Cập nhật trong cơ sở dữ liệu
    }

    @Override
    public List<Users> findByRolePatient() {
        // Tìm Role có tên "ROLE_PATIENT"
        Role patientRole = roleRepository.findByRoleName("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("Role 'ROLE_PATIENT' không tồn tại"));

        // Tìm tất cả người dùng có Role "ROLE_PATIENT"
        return userRepository.findByRole(patientRole);
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
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setGender(patientDto.getGender());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        // 4. Lưu Patient vào bảng `patients`
        patientRepository.save(patient);
    }


}
