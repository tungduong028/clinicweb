package com.example.clinicweb.controller;

import com.example.clinicweb.dto.RegisterUserDTO;
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
        model.addAttribute("registerUser", new RegisterUserDTO()); // Gán DTO vào model cho form
        return "register"; // Điều hướng đến trang register.html
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUser") RegisterUserDTO registerUserDTO) {
        userService.register(registerUserDTO); // Gọi service để đăng ký người dùng
        return "redirect:/login"; // Sau khi đăng ký thành công, chuyển hướng đến trang login
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Điều hướng đến trang login.html
    }
}

