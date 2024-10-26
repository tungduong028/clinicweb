package com.example.clinicweb.controller;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserServiceImpl userService;

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UsersDTO());  // Khởi tạo UsersDTO cho form
        model.addAttribute("patient", new PatientDTO());  // Khởi tạo PatientDTO cho form
        return "register"; // Điều hướng đến trang register.html
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") UsersDTO usersDTO,
            @ModelAttribute("patient") PatientDTO patientDTO) {

        // Gọi service để đăng ký người dùng và bệnh nhân
        userService.register(usersDTO, patientDTO);

        // Sau khi đăng ký thành công, chuyển hướng đến trang login
        return "redirect:/login";
    }

}
