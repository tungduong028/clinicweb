package com.example.clinicweb.repository;

import com.example.clinicweb.model.Admin;
import com.example.clinicweb.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

}