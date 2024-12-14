package com.example.clinicweb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication, Model model) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else {
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

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // Trả về trang giao diện forgot-password.html
    }
}

