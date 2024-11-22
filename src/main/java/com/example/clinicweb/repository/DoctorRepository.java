package com.example.clinicweb.repository;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Tìm kiếm bác sĩ dựa trên tên, email hoặc chuyên khoa (case insensitive)
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchDoctors(String keyword);
}
