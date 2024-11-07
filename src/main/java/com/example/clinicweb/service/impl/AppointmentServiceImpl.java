package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.Appointment;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.repository.AppointmentRepository;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public void bookAppointment(AppointmentDTO appointmentDTO) {
        // Tìm kiếm Doctor và Patient dựa trên ID
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Kiểm tra nếu bác sĩ đã có lịch hẹn vào ngày và giờ này
        boolean isSlotTaken = appointmentRepository.findByDoctor_DoctorIdAndDateAndTime(
                appointmentDTO.getDoctorId(), appointmentDTO.getDate(), appointmentDTO.getTime()).isPresent();
        if (isSlotTaken) {
            throw new RuntimeException("This time slot is already booked for the selected doctor.");
        }

        // Tạo đối tượng Appointment và lưu vào database
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTime(appointmentDTO.getTime());
        appointment.setStatus(Appointment.Status.SCHEDULED);

        appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public void cancelAppointmentById(Long id) {
        appointmentRepository.deleteById(id);
    }
}
