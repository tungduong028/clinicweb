package com.example.clinicweb.controller;

import com.example.clinicweb.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    DoctorService doctorService;

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");
        model.addAttribute("doctors", doctorService.findAllDoctors());
        return "about";
    }

    @GetMapping("/team")
    public String team(Model model) {
        model.addAttribute("title", "team");
        model.addAttribute("doctors", doctorService.findAllDoctors());
        return "team";
    }
    @GetMapping("/appointment")
    public String appointment(Model model) {
        model.addAttribute("title", "appointment");
        return "appointment";
    }
    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("title", "search");
        model.addAttribute("doctors", doctorService.findAllDoctors());
        return "search";
    }
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "contact");
        return "contact";
    }

}
