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

    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> editUser(@RequestBody Map<String, String> payload) {
        try {
            Long userId = Long.parseLong(payload.get("id"));
            String role = payload.get("role");

            if (!role.equals("Admin") && !role.equals("Doctor") && !role.equals("Patient")) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Vai trò không hợp lệ"));
            }

            Users user = userService.findById(userId);
            if (user != null) {
                Role newRole = roleRepository.findByRoleName(role);
                if (newRole == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Vai trò không tồn tại"));
                }

                user.setRole(newRole);
                userService.save(user);

                return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật vai trò thành công"));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Người dùng không tồn tại"));
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "Đã xảy ra lỗi"));
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("id"));
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            // Log exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }
    }
}