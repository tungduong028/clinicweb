package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.Appointment;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.WorkingDays;
import com.example.clinicweb.repository.AppointmentRepository;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.WorkingHoursRepository;
import com.example.clinicweb.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.Date;
import java.time.DayOfWeek;
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

    @Autowired
    private WorkingHoursRepository workingHoursRepository;

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
        Patient patient = patientRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân !"));

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
        List<WorkingDays> workingHours = workingHoursRepository.findByDoctor_DoctorIdAndDayOfWeek(
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
        appointment.setPatient(patient); // Gán patient lấy từ thông tin đăng nhập
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
