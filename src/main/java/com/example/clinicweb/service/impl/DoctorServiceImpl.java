package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.DoctorService;
import com.example.clinicweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    public void saveDoctor(DoctorDTO doctorDto){
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorDto.getDoctorId());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setGender(doctorDto.getGender());
        Optional<Users> user = usersRepository.findById(doctorDto.getUserId());
        user.ifPresent(value -> doctor.setUser(value));
        doctor.setFullName(doctorDto.getFullName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setExperienceYears(doctorDto.getExperienceYears());
        doctor.setDateOfBirth(doctorDto.getDateOfBirth());
        doctor.setPhoneNumber(doctorDto.getPhoneNumber());
        doctor.setImageUrl(doctorDto.getImageUrl());
        doctorRepository.save(doctor);
    }

    @Override
    public DoctorDTO toDoctorDto(Doctor doctorDto) {
        DoctorDTO doctor = new DoctorDTO();
        doctor.setDoctorId(doctorDto.getDoctorId());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setGender(doctorDto.getGender());
        doctor.setUserId(doctorDto.getUser().getUserId());
        doctor.setFullName(doctorDto.getFullName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setExperienceYears(doctorDto.getExperienceYears());
        doctor.setDateOfBirth(doctorDto.getDateOfBirth());
        doctor.setPhoneNumber(doctorDto.getPhoneNumber());
        doctor.setImageUrl(doctorDto.getImageUrl());
        return doctor;
    }

    @Override
    public void saveNewDoctor(DoctorDTO doctorDto, UsersDTO userDto){
        userDto.setRoleName("ROLE_DOCTOR");
        Users savedUser = userService.saveUser(userDto);

        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorDto.getDoctorId());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setGender(doctorDto.getGender());
        doctor.setUser(savedUser);
        doctor.setFullName(doctorDto.getFullName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setExperienceYears(doctorDto.getExperienceYears());
        doctor.setDateOfBirth(doctorDto.getDateOfBirth());
        doctor.setPhoneNumber(doctorDto.getPhoneNumber());
        doctor.setImageUrl(doctorDto.getImageUrl());
        doctorRepository.save(doctor);
    }

    public Page<Doctor> findAll(Pageable pageable){return doctorRepository.findAll(pageable);}

    public Optional<Doctor> findById(Long id){ return doctorRepository.findById(id);}

    public Page<Doctor> findByIsDeletedFalseAndFullNameContainingIgnoreCase(String keyword, Pageable pageable){ return doctorRepository.findByIsDeletedFalseAndFullNameContainingIgnoreCase(keyword, pageable);}

    public int markAsDeleted(Long id){
        return doctorRepository.markAsDeleted(id);
    }

    public Page<Doctor> findByIsDeletedFalse(Pageable pageable){ return doctorRepository.findByIsDeletedFalse(pageable);}
}
