package com.example.clinicweb.service;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Users;

import java.util.List;

public interface DoctorService {
    List<Doctor> findAllDoctors();
    List<Doctor> getAllDoctors(); // Lấy danh sách bác sĩ
    String saveDoctor(Doctor doctor); // Thêm bác sĩ
    Doctor getDoctorById(Long id); // Lấy bác sĩ theo ID
    void updateDoctor(Doctor doctor); // Cập nhật bác sĩ
    void deleteDoctor(Long id); // Xóa bác sĩ
    List<Doctor> searchDoctors(String keyword); // Tìm kiếm bác sĩ theo từ khóa
}
