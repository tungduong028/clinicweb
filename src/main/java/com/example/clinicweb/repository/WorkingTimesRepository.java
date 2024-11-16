package com.example.clinicweb.repository;

import com.example.clinicweb.model.WorkingTimes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingTimesRepository extends JpaRepository<WorkingTimes, Long> {
    WorkingTimes findByDoctor_DoctorId(Long doctorId);
}

