package com.example.clinicweb.service;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    List<Doctor> findAllDoctors();

    void saveDoctor(DoctorDTO service);

    void saveNewDoctor(DoctorDTO doctorDto, UsersDTO userDto);

    Page<Doctor> findAll(Pageable pageable);

    DoctorDTO toDoctorDto(Doctor doctorDto);

    Optional<Doctor> findById(Long id);

    Page<Doctor> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String keyword, Pageable pageable);

    int markAsDeleted(Long id);

    Page<Doctor> findByIsDeletedFalse(Pageable pageable);
}
