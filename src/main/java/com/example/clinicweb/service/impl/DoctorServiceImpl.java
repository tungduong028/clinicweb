package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.DoctorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;



    @Override
    public List<DoctorDTO> findAllDoctors() {
        List<Doctor> doctors = doctorRepository.findByIsDeletedFalse();
        return doctors.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public List<DoctorDTO> searchDoctors(String keyword) {
        List<Doctor> doctors = doctorRepository.findByFullNameContainingIgnoreCase(keyword);
        return doctors.stream()
                .map(this::convertToDTO)  // Sử dụng phương thức convertToDTO để chuyển đổi
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> searchDoctorsByName(String name) {
        List<Doctor> doctors = doctorRepository.findByFullNameContainingIgnoreCase(name);
        return doctors.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public DoctorDTO findDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        return convertToDTO(doctor);
    }

    @Override
    public void saveDoctor(DoctorDTO doctorDTO) {
        // Kiểm tra xem các trường bắt buộc có hợp lệ không
        if (doctorDTO.getFullName() == null || doctorDTO.getEmail() == null || doctorDTO.getExperienceYears() == null || doctorDTO.getSpecialization() == null ) {
            throw new IllegalArgumentException("Full name, email, specialization, experience years, and room id are required");
        }

        // Tìm Role từ RoleRepository (ROLE_DOCTOR)
        Optional<Role> roleOptional = roleRepository.findByRoleName("ROLE_DOCTOR");
        if (!roleOptional.isPresent()) {
            throw new IllegalArgumentException("Role not found");
        }

        // Kiểm tra logic (ví dụ: kiểm tra trùng username/email)
        if (userRepository.findByUsername(doctorDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + doctorDTO.getEmail());
        }

        try {

            // Tạo đối tượng User mới
            Users user = new Users();
            user.setUsername(doctorDTO.getFullName());
            user.setPasswordHash("defaultPasswordHash");
            user.setRole(roleOptional.get());

            // Lưu User vào cơ sở dữ liệu để tạo userId
            userRepository.save(user);

            // Tạo đối tượng Doctor từ DTO
            Doctor doctor = new Doctor();
            doctor.setFullName(doctorDTO.getFullName());
            doctor.setEmail(doctorDTO.getEmail());
            doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
            doctor.setGender(doctorDTO.getGender());
            doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
            doctor.setSpecialization(doctorDTO.getSpecialization());
            doctor.setExperienceYears(doctorDTO.getExperienceYears());
            doctor.setRoomId(doctorDTO.getRoomId());

            // Liên kết User với Doctor
            doctor.setUser(user);

            // Lưu Doctor vào cơ sở dữ liệu
            doctorRepository.save(doctor);

        } catch (Exception e) {
            // Nếu xảy ra lỗi, transaction sẽ rollback
            throw new RuntimeException("Failed to save doctor", e);
        }
    }


    @Override
    public void updateDoctor(Long doctorId, DoctorDTO doctorDTO) {
        // Tìm bác sĩ theo ID
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        // Kiểm tra xem các trường bắt buộc có hợp lệ không
        if (doctorDTO.getFullName() == null || doctorDTO.getEmail() == null || doctorDTO.getExperienceYears() == null || doctorDTO.getSpecialization() == null) {
            throw new IllegalArgumentException("Full name, email, specialization, and experience years are required");
        }

        // Cập nhật thông tin bác sĩ
        doctor.setFullName(doctorDTO.getFullName());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setGender(doctorDTO.getGender());
        doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setExperienceYears(doctorDTO.getExperienceYears());
        doctor.setRoomId(doctorDTO.getRoomId());

        // Nếu thông tin user (username) được chỉnh sửa
        if (doctorDTO.getUserId() != null) {
            Users user = userRepository.findById(doctorDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + doctorDTO.getUserId()));
            user.setUsername(doctorDTO.getFullName()); // Cập nhật username
            userRepository.save(user); // Lưu user vào cơ sở dữ liệu
        }

        // Lưu lại thông tin bác sĩ
        doctorRepository.save(doctor);
    }



    @Override
    public Optional<DoctorDTO> findById(Long doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        return doctor.map(this::convertToDTO); // Chuyển đổi Doctor entity sang DTO
    }




    // Xóa bác sĩ
    @Override
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        doctor.setDeleted(true); // Đánh dấu là đã xóa
        doctorRepository.save(doctor); // Cập nhật trong cơ sở dữ liệu
    }




    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setFullName(doctor.getFullName());
        dto.setEmail(doctor.getEmail());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setGender(doctor.getGender());
        dto.setDateOfBirth(doctor.getDateOfBirth());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setRoomId(doctor.getRoomId());
        dto.setUserId(doctor.getUser().getUserId());
        return dto;
    }


//    @Override
//    public Page<DoctorDTO> findDoctorsWithPagination(Pageable pageable) {
//        Page<Doctor> doctorsPage = doctorRepository.findAll(pageable); // Hoặc phương thức khác nếu bạn muốn tìm bác sĩ theo các điều kiện khác.
//        // Chuyển đổi từ Doctor entity sang DoctorDTO
//        return doctorsPage.map(doctor -> convertToDTO(doctor));
//    }

    @Override
    public Page<DoctorDTO> searchDoctorsByNameWithPagination(String name, Pageable pageable) {
        Page<Doctor> doctorPage = doctorRepository.findByFullNameContainingIgnoreCase(name, pageable);
        return doctorPage.map(this::convertToDTO);
    }

    @Override
    public Page<DoctorDTO> findAllActiveDoctors(Pageable pageable) {
        // Lấy danh sách bác sĩ chưa bị xóa từ DB
        Page<Doctor> activeDoctors = doctorRepository.findByIsDeletedFalse(pageable);

        // Chuyển đổi sang DTO trước khi trả về
        return activeDoctors.map(this::convertToDTO);
    }



}