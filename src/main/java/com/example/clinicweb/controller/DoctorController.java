package com.example.clinicweb.controller;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.UsersRepository;
import com.example.clinicweb.service.DoctorService;
import com.example.clinicweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private UserService userService;
    @Autowired
    private UsersRepository usersRepository;

    // Hiển thị danh sách bác sĩ
    @GetMapping
    public String listDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        if (doctors.isEmpty()) {
            model.addAttribute("message", "Chưa có dữ liệu bác sĩ. Hãy thêm bác sĩ mới.");
        }
        return "admin/doctor"; // Trả về file doctor.html
    }

    // Xử lý thêm bác sĩ mới
    @PostMapping("/save")
    public ResponseEntity<List<Doctor>> addDoctor(@ModelAttribute Doctor doctor) {
        try {
            String username;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
            Users users = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));
            doctor.setUser(users);

            // Thêm bác sĩ mới
            doctorService.saveDoctor(doctor);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Trả về mã lỗi 500 nếu có lỗi
        }

        // Trả về danh sách bác sĩ sau khi thêm
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);  // Trả về danh sách bác sĩ mới cập nhật
    }

    // Chỉnh sửa bác sĩ
    @GetMapping("/edit/{doctorId}")
    @ResponseBody
    public Doctor editDoctor(@PathVariable Long doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @PostMapping("/update")
    public ResponseEntity<List<Doctor>> updateDoctor(@ModelAttribute Doctor doctor) {
        try {
            doctorService.updateDoctor(doctor);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Trả về mã lỗi 500 nếu có lỗi
        }

        // Trả về danh sách bác sĩ sau khi cập nhật
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // Xóa bác sĩ
    @DeleteMapping("/delete/{doctorId}")
    public ResponseEntity<List<Doctor>> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);

        // Trả về danh sách bác sĩ sau khi xóa
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam("username") String keyword, Model model) {
        List<Doctor> doctors;
        if (keyword != null && !keyword.isEmpty()) {
            doctors = doctorService.searchDoctors(keyword);
            model.addAttribute("doctors", doctors);
            model.addAttribute("searchKeyword", keyword);
        } else {
            doctors = doctorService.getAllDoctors();
            model.addAttribute("doctors", doctors);
        }

        if (doctors.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy bác sĩ nào với từ khóa: " + keyword);
        }
        return ResponseEntity.ok(doctors); // Trả về cùng trang danh sách bác sĩ
    }

}



