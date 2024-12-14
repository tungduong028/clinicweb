package com.example.clinicweb.controller;

import com.example.clinicweb.dto.AppointmentDTO;
import com.example.clinicweb.model.Service;
import com.example.clinicweb.service.*;
import com.example.clinicweb.service.impl.DoctorServiceImpl;
import com.example.clinicweb.service.impl.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("title", "Admin Page");
        return "admin/index";
    }

    @GetMapping("/index")
    public String homepage(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("appointment", new AppointmentDTO());
        model.addAttribute("doctors", doctorService.findAllDoctors());
        model.addAttribute("patients", patientService.findAllPatients());
        try {
            List<Service> services = new ArrayList<Service>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Service> pageTuts;
            pageTuts = serviceService.findByIsDeletedFalse(paging);

            services = pageTuts.getContent();

            model.addAttribute("services", services);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "index";
    }
}
