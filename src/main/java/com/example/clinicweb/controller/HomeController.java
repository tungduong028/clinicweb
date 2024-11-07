package com.example.clinicweb.controller;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.service.impl.DoctorServiceImpl;
import com.example.clinicweb.service.impl.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private DoctorServiceImpl doctorService;

    @Autowired
    private PatientServiceImpl patientService;

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("title", "Admin Page");
        return "admin/index";
    }

    @GetMapping("/index")
    public String homepage(Model model) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("appointment", new AppointmentDTO());
        model.addAttribute("doctors", doctorService.findAllDoctors());
        model.addAttribute("patients", patientService.findAllPatients());
        return "index";
    }
}
