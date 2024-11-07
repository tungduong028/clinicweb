package com.example.clinicweb.repository;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
