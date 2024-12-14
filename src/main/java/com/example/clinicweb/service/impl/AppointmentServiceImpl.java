package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.*;
import com.example.clinicweb.repository.AppointmentRepository;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.WorkingDaysRepository;
import com.example.clinicweb.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WorkingDaysRepository workingDaysRepository;

    @Override
    @Transactional
    public void bookAppointment(AppointmentDTO appointmentDTO) {
        // Lấy username từ người dùng đang đăng nhập
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Tìm `Patient` dựa trên `username`
        Patient patient;
        try {
            patient = patientRepository.findByUser_Username(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân !"));
        }
        catch (Exception e) {
            patient = patientRepository.findById(appointmentDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân !"));
        }

        // Tìm kiếm Doctor dựa trên ID
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ !"));

        // Kiểm tra nếu ngày đặt lịch là ngày trong tương lai và tối đa là 3 tháng tới
        LocalDate appointmentDate = appointmentDTO.getDate();
        LocalDate currentDate = LocalDate.now();
        LocalDate maxDate = currentDate.plusMonths(3);

        if (appointmentDate.isBefore(currentDate)) {
            throw new RuntimeException("Ngày đặt lịch phải là ngày trong tương lai");
        }
        if (appointmentDate.isAfter(maxDate)) {
            throw new RuntimeException("Ngày đặt lịch không được quá 3 tháng từ hôm nay");
        }

        // Kiểm tra ngày làm việc của bác sĩ
        DayOfWeek appointmentDayOfWeek = appointmentDate.getDayOfWeek();
        List<WorkingDays> workingHours = workingDaysRepository.findByDoctor_DoctorIdAndDayOfWeek(
                appointmentDTO.getDoctorId(), appointmentDayOfWeek);

        if (workingHours.isEmpty()) {
            throw new RuntimeException("Bác sĩ không làm việc vào ngày này !");
        }

        // Kiểm tra nếu bác sĩ đã có lịch hẹn vào ngày và giờ này
        boolean isSlotTaken = appointmentRepository.findByDoctor_DoctorIdAndDateAndTime(
                appointmentDTO.getDoctorId(), Date.valueOf(appointmentDTO.getDate()), appointmentDTO.getTime()).isPresent();
        if (isSlotTaken) {
            throw new RuntimeException("Khoảng thời gian khám của bác sĩ này đã bận");
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
    @Transactional
    public void updateAppointment(Long appointmentId, AppointmentDTO appointmentDTO) {
        // Tìm kiếm Appointment theo ID
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        // Tìm kiếm bác sĩ mới nếu có thay đổi
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ!"));

        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân !"));
        // Lấy thông tin ngày giờ mới
        LocalDate appointmentDate = appointmentDTO.getDate();
        LocalDate currentDate = LocalDate.now();
        LocalDate maxDate = currentDate.plusMonths(3);

        // Kiểm tra ngày đặt lịch phải hợp lệ (trong tương lai và không quá 3 tháng)
        if (appointmentDate.isBefore(currentDate)) {
            throw new RuntimeException("Ngày đặt lịch phải là ngày trong tương lai");
        }
        if (appointmentDate.isAfter(maxDate)) {
            throw new RuntimeException("Ngày đặt lịch không được quá 3 tháng từ hôm nay");
        }

        // Kiểm tra ngày làm việc của bác sĩ
        DayOfWeek appointmentDayOfWeek = appointmentDate.getDayOfWeek();
        List<WorkingDays> workingHours = workingDaysRepository.findByDoctor_DoctorIdAndDayOfWeek(
                appointmentDTO.getDoctorId(), appointmentDayOfWeek);

        if (workingHours.isEmpty()) {
            throw new RuntimeException("Bác sĩ không làm việc vào ngày này!");
        }

        // Kiểm tra nếu bác sĩ đã có lịch hẹn vào ngày và giờ này (trừ khi đó là lịch hẹn hiện tại)
        boolean isSlotTaken = appointmentRepository.findByDoctor_DoctorIdAndDateAndTime(
                        appointmentDTO.getDoctorId(), Date.valueOf(appointmentDTO.getDate()), appointmentDTO.getTime())
                .map(existingAppointment -> !existingAppointment.getAppointmentId().equals(appointmentId))
                .orElse(false);
        if (isSlotTaken) {
            throw new RuntimeException("Khoảng thời gian khám của bác sĩ này đã bận");
        }

        // Cập nhật thông tin lịch hẹn
        appointment.setDoctor(doctor);
        appointment.setDate(Date.valueOf(appointmentDTO.getDate()));
        appointment.setTime(appointmentDTO.getTime());
        appointment.setPatient(patient);
        appointment.setStatus(Appointment.Status.SCHEDULED);

        // Lưu thay đổi vào database
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        appointment.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public Page<Appointment> findByDoctor_DoctorId(Long id, Pageable pageable){
        return appointmentRepository.findByDoctor_DoctorId(id, pageable);
    }

    @Override
    public Page<Appointment> findByIsDeletedFalse(Pageable pageable){
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable){
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public Optional<Appointment> findById(Long id){
        return appointmentRepository.findById(id);
    }
}
