package com.example.clinicweb.service;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.Appointment;

import java.util.List;

public interface AppointmentService {


    List<Appointment> findAllAppointments();
    void cancelAppointmentById(Long id);
}
