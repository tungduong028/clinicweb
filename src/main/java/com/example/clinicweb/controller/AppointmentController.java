package com.example.clinicweb.controller;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.service.impl.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentServiceImpl appointmentService;

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
        return "redirect:/index";
    }
}
