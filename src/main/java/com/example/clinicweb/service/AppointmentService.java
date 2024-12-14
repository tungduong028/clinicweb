package com.example.clinicweb.service;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppointmentService {

    void bookAppointment(AppointmentDTO appointmentDTO);

    void updateAppointment(Long appointmentId, AppointmentDTO appointmentDTO);

    void cancelAppointment(Long id);

    Page<Appointment> findAll(Pageable paging);

    Page<Appointment> findByIsDeletedFalse(Pageable pageable);

    Page<Appointment> findByDoctor_DoctorId(Long keyword, Pageable paging);

    Optional<Appointment> findById(Long id);
}
