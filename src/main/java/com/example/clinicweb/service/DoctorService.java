package com.example.clinicweb.service;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<DoctorDTO> findAllDoctors();

    List<DoctorDTO> searchDoctorsByName(String name);


    DoctorDTO findDoctorById(Long doctorId);
    Optional<DoctorDTO> findById(Long doctorId);

    List<DoctorDTO> searchDoctors(String keyword); // Tìm kiếm bác sĩ theo từ khóa

    void saveDoctor(DoctorDTO doctorDTO);

    void deleteDoctor(Long doctorId);

    void updateDoctor(Long doctorId, DoctorDTO doctorDTO);

    // Thêm phương thức phân trang cho danh sách bác sĩ
//    Page<DoctorDTO> findDoctorsWithPagination(Pageable pageable);
    Page<DoctorDTO> searchDoctorsByNameWithPagination(String name, Pageable pageable);
    Page<DoctorDTO> findAllActiveDoctors(Pageable pageable);

}