package com.example.clinicweb.controller;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.Appointment;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.model.WorkingTimes;
import com.example.clinicweb.service.impl.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // Lấy danh sách các mốc thời gian dựa trên bác sĩ được chọn
    @GetMapping("/get-available-times")
    @ResponseBody
    public List<LocalTime> getAvailableTimes(@RequestParam("doctorId") Long doctorId) {
        WorkingTimes workingTimes = workingTimesService.findByDoctor_DoctorId(doctorId);
        List<LocalTime> availableTimes = new ArrayList<>();
        if (workingTimes != null) {
            LocalTime start = workingTimes.getTimeStart();
            LocalTime end = workingTimes.getTimeEnd();
            while (start.isBefore(end)) {
                availableTimes.add(start);
                start = start.plusHours(1);  // Tăng thêm 1 giờ mỗi lần
            }
        }
        return availableTimes;
    }

    @GetMapping("/admin/appointment")
    public String getAll(Model model, @RequestParam(required = false) Long keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            List<Appointment> appointments = new ArrayList<Appointment>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Appointment> pageTuts;
            if (keyword == null) {
                pageTuts = appointmentService.findAll(paging);
            } else {
                pageTuts = appointmentService.findByDoctor_DoctorId(keyword, paging);
                model.addAttribute("keyword", keyword);
            }
            appointments = pageTuts.getContent();

            model.addAttribute("appointments", appointments);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "admin/appointment";
    }

    @PostMapping("/admin/appointment/new")
    public String newAppointments(
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
        return "admin/appointment/appointment_form";
    }

    @GetMapping("/admin/appointment/new")
    public String newAppointment(Model model) {
        model.addAttribute("doctors", doctorService.findAllDoctors());
        model.addAttribute("patients", patientService.findAllPatients());
        return "admin/appointment/appointment_form";
    }
    @GetMapping("/admin/appointment/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        // Tìm lịch hẹn theo ID
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));
        // Đưa dữ liệu vào model
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.findAllDoctors());
        model.addAttribute("patients", patientService.findAllPatients());
        model.addAttribute("pageTitle", "Chỉnh sửa lịch khám (#" + id + ")");

        return "admin/appointment/appointment_edit";
    }
    @PostMapping("/admin/appointment/edit/{id}")
    public String UpdateForm(@PathVariable("id") Long id, @ModelAttribute("appointment") AppointmentDTO appointmentDTO, Model model) {
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.findAllDoctors());
        model.addAttribute("patients", patientService.findAllPatients());
        try {
            appointmentService.updateAppointment(id, appointmentDTO);
            model.addAttribute("successMessage", "Lịch hẹn đã được đặt thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/appointment";
    }
    @PostMapping("/admin/appointment/delete/{id}")
    public String cancelAppointment(@PathVariable("id") Long id, Model model) {
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        // Cập nhật trạng thái của lịch hẹn thành CANCELLED
        try {
            appointmentService.cancelAppointment(id);
            model.addAttribute("successMessage", "Lịch hẹn đã bị hủy thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/appointment";
    }
}
