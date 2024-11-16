package com.example.clinicweb.controller;

import com.example.clinicweb.service.impl.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private PatientServiceImpl patientService;

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication, Model model) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else {
//            model.addAttribute("patients", patientService.findPatientByUsername(authentication.getName()));
            return "redirect:/index";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "error", required = false) Boolean error) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @GetMapping("/login?logout")
    public String logoutPage() {
        return "/login";
    }
}

