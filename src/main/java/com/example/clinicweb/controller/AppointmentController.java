package com.example.clinicweb.controller;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.WorkingTimes;
import com.example.clinicweb.service.impl.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppointmentController {
    @Autowired
    private DoctorServiceImpl doctorService;

    @Autowired
    private PatientServiceImpl patientService;

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private WorkingTimesServiceImpl workingTimesService;

    // Xử lý đặt lịch khám
    @PostMapping("/new-booking")
    public String bookAppointment(
            @ModelAttribute("appointment") AppointmentDTO appointmentDTO,
            Model model) {
        try {
            appointmentService.bookAppointment(appointmentDTO);
            model.addAttribute("successMessage", "Lịch hẹn đã được đặt thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("doctors", doctorService.findAllDoctors());
        model.addAttribute("patients", patientService.findAllPatients());
        return "index";
    }

    // Hàm này sẽ lấy danh sách các mốc thời gian dựa trên bác sĩ được chọn
    @GetMapping("/get-available-times")
    @ResponseBody
    public List<LocalTime> getAvailableTimes(@RequestParam("doctorId") Long doctorId) {
        WorkingTimes workingTimes = workingTimesService.findByDoctor_DoctorId(doctorId);
        List<LocalTime> availableTimes = new ArrayList<>();
        if (workingTimes != null) {
            LocalTime start = workingTimes.getTimeStart();
            LocalTime end = workingTimes.getTimeEnd();
            while (!start.isAfter(end)) {
                availableTimes.add(start);
                start = start.plusHours(1);  // Tăng thêm 1 giờ mỗi lần
            }
        }
        return availableTimes;
    }
}
