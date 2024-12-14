package com.example.clinicweb.repository;

import com.example.clinicweb.model.Appointment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Tìm kiếm lịch hẹn của bác sĩ vào ngày và thời gian cụ thể
    Optional<Appointment> findByDoctor_DoctorIdAndDateAndTime(Long doctorId, Date date, LocalTime time);

    Page<Appointment> findByDoctor_DoctorId(Long id, Pageable pageable);
}
