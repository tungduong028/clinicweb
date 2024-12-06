package com.example.clinicweb.controller;

import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.service.EmailService;
import com.example.clinicweb.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Patient patient = patientService.findByEmail(email);
        if (patient == null) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống.");
            return "forgot-password";
        }
        String token = UUID.randomUUID().toString();
        patientService.updateResetPasswordToken(token, email);

        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendEmail(email, "Đặt lại mật khẩu",
                "Nhấp vào liên kết sau để đặt lại mật khẩu của bạn: " + resetUrl);
        model.addAttribute("message", "Một email đặt lại mật khẩu đã được gửi.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("user", new UsersDTO());
        Users users = patientService.getByResetPasswordToken(token);
        if (users == null) {
            model.addAttribute("error", "Token không hợp lệ.");
            return "reset-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String newPassword,
                                       Model model) {
        Users users = patientService.getByResetPasswordToken(token);
        if (users == null) {
            model.addAttribute("error", "Token không hợp lệ.");
            return "reset-password";
        }
        patientService.updatePassword(users, newPassword);
        model.addAttribute("message", "Đổi mật khẩu thành công.");
        return "login";
    }
}
