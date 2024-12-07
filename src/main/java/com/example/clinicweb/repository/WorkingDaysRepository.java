package com.example.clinicweb.repository;

import com.example.clinicweb.model.Appointment;
import com.example.clinicweb.model.WorkingDays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface WorkingDaysRepository extends JpaRepository<WorkingDays, Integer> {
    List<WorkingDays> findByDoctor_DoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
    Page<WorkingDays> findByDoctor_DoctorId(Long id, Pageable pageable);

}
