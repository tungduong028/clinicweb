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

import java.sql.Date;
import java.time.LocalDate;
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
                appointmentDTO.getDoctorId(), Date.valueOf(appointmentDTO.getDate()), appointmentDTO.getTime()).isPresent();
        if (isSlotTaken) {
            throw new RuntimeException("Khung giờ khám này của bác sĩ đã bận !");
        }

        // Kiểm tra nếu ngày đặt lịch là ngày trong tương lai và tối đa là 3 tháng tới
        LocalDate appointmentDate = appointmentDTO.getDate();
        LocalDate currentDate = LocalDate.now();
        LocalDate maxDate = currentDate.plusMonths(3);  // Giới hạn 3 tháng tới

        if (appointmentDate.isBefore(currentDate)) {
            throw new RuntimeException("Ngày đặt lịch phải là ngày trong tương lai");
        }

        if (appointmentDate.isAfter(maxDate)) {
            throw new RuntimeException("Ngày đặt lịch không được quá 3 tháng từ hôm nay");
        }

        // Tạo đối tượng Appointment và lưu vào database
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDate(Date.valueOf(appointmentDTO.getDate()));
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
