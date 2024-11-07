package com.example.clinicweb.service;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Users;

import java.util.List;

public interface DoctorService {

    List<Doctor> findAllDoctors();
}
