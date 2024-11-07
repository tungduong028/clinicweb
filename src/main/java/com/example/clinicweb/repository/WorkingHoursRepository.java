package com.example.clinicweb.repository;

import com.example.clinicweb.model.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Integer> {

    Optional<WorkingHours> findByDoctorDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
}
