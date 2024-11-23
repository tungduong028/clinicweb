package com.example.clinicweb.controller;

import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.clinicweb.repository.RoleRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String listUsers(Model model) {
       List<Users> users = userService.getUsers();
       model.addAttribute("users", users);
        return "admin/user"; // Trả về view user.html
    }

    @GetMapping("/search")
    public ResponseEntity<List<Users>> searchUsers(@RequestParam String keyword) {
        List<Users> users = userService.searchUsersByKeyword(keyword);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public String addUser (@ModelAttribute Users user) {
        userService.save(user);
        return "redirect:/admin/user"; // Quay lại danh sách người dùng
    }

    // Phương thức xóa người dùng
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deleteUserById(userId);
            response.put("success", true);
            response.put("message", "Người dùng đã được xóa thành công.");
        } catch (RuntimeException ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
