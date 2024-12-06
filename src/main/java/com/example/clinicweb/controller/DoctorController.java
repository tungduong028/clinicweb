package com.example.clinicweb.controller;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.service.DoctorService;
import com.example.clinicweb.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
    private RoleRepository roleRepository;


//    @GetMapping
//    public String listDoctors(Model model) {
//        List<DoctorDTO> doctors = doctorService.findAllDoctors();
//        model.addAttribute("doctors", doctors);
//        return "admin/doctor"; // Tên file HTML
//    }

    @GetMapping("/search")
    public String searchDoctors(
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "type", defaultValue = "fullname") String type,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // Điều chỉnh giá trị page để luôn >= 1
        page = Math.max(page, 1);

        // Giảm `page` đi 1 vì `Pageable` sử dụng chỉ số bắt đầu từ 0
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("fullName").ascending());

        Page<DoctorDTO> doctorPage;

        // Thực hiện tìm kiếm dựa trên từ khóa và loại tìm kiếm
        if (query != null && !query.isEmpty()) {
            switch (type) {
                case "fullname":
                    doctorPage = doctorService.searchDoctorsByNameWithPagination(query, pageable);
                    break;
                default:
                    doctorPage = doctorService.findAllActiveDoctors(pageable);
            }
        } else {
            doctorPage = doctorService.findAllActiveDoctors(pageable);
        }

        // Thêm các thuộc tính cần thiết vào model để hiển thị trong giao diện
        model.addAttribute("doctors", doctorPage);
        model.addAttribute("currentPage", doctorPage.getNumber() + 1);
        model.addAttribute("totalItems", doctorPage.getTotalElements());
        model.addAttribute("totalPages", doctorPage.getTotalPages());
        model.addAttribute("pageSize", doctorPage.getSize());
        model.addAttribute("searchKeyword", query);
        model.addAttribute("type", type);

        // Trả về tên file HTML (trang hiển thị danh sách bác sĩ)
        return "admin/doctor";
    }





    @PostMapping("/save")
    @ResponseBody
    public List<DoctorDTO> saveDoctor(@ModelAttribute DoctorDTO doctorDTO) {
        try {
            doctorService.saveDoctor(doctorDTO);  // Lưu bác sĩ
            return doctorService.findAllDoctors();  // Trả về danh sách bác sĩ mới nhất
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving doctor", e);
        }
    }


    @PostMapping("/update")
    @ResponseBody
    public List<DoctorDTO> updateDoctor(@ModelAttribute DoctorDTO doctorDTO) {
        try {
            // Kiểm tra xem Doctor ID có được cung cấp không
            if (doctorDTO.getDoctorId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor ID cannot be null");
            }

            // Gọi service để cập nhật thông tin bác sĩ
            doctorService.updateDoctor(doctorDTO.getDoctorId(), doctorDTO);

            // Trả về danh sách bác sĩ mới nhất sau khi cập nhật
            return doctorService.findAllDoctors();
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log chi tiết lỗi
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating doctor: " + e.getMessage(), e);
        }
    }




    // Xóa bác sĩ
    @DeleteMapping("/delete/{doctorId}")
    public ResponseEntity<List<DoctorDTO>> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);

        // Trả về danh sách bác sĩ chưa bị xóa
        List<DoctorDTO> doctors = doctorService.findAllDoctors();
        return ResponseEntity.ok(doctors);
    }



    @GetMapping("/edit/{doctorId}")
    @ResponseBody
    public DoctorDTO getDoctorById(@PathVariable Long doctorId) {
        Optional<DoctorDTO> doctor = doctorService.findById(doctorId);
        if (doctor.isPresent()) {
            return doctor.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found with ID: " + doctorId);
        }
    }

    @GetMapping()
    public String listDoctors(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // Điều chỉnh giá trị page để luôn >= 1
        page = Math.max(page, 1);

        Pageable pageable = PageRequest.of(page - 1, size );
        // Lấy danh sách bác sĩ chưa bị xóa
        Page<DoctorDTO> doctorPage = doctorService.findAllActiveDoctors(pageable);

        model.addAttribute("doctors", doctorPage);
        model.addAttribute("currentPage", doctorPage.getNumber() + 1);
        model.addAttribute("totalItems", doctorPage.getTotalElements());
        model.addAttribute("totalPages", doctorPage.getTotalPages());
        model.addAttribute("pageSize", doctorPage.getSize());
//        System.err.println(doctorPage.getTotalPages());
//        System.err.println(doctorPage.getSize());
        return "admin/doctor";
    }



}