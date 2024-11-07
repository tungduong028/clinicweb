package com.example.clinicweb.service.impl;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }
}
