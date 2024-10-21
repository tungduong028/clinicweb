package com.example.clinicweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("title", "Admin Page");
        return "admin"; // Đảm bảo rằng admin.html tồn tại trong templates/admin
    }

    @GetMapping("/index")
    public String homepage(Model model) {
        model.addAttribute("title", "Home Page");
        return "index"; // Đảm bảo rằng homepage.html tồn tại trong templates
    }
}
