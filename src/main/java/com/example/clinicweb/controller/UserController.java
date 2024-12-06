package com.example.clinicweb.controller;

//import ch.qos.logback.core.model.Model;
import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorRepository doctorRepository; // Inject DoctorRepository

    @Autowired
    private PatientRepository patientRepository; // Inject PatientRepository

    // Xử lý thêm người dùng
    @PostMapping("/save")
    public String addUser( @RequestParam("user.username") String username,
                           @RequestParam("user.password") String password,
                           @RequestParam("user.roleName") String roleName,
                           @RequestParam("doctor.fullName") String fullName,
                           @RequestParam("doctor.email") String email,
                           @RequestParam("doctor.phoneNumber") String phoneNumber,
                           @RequestParam("doctor.gender") String gender,
                           @RequestParam("doctor.dateOfBirth") String dateOfBirth,
                           @RequestParam("doctor.specialization") String specialization,
                           @RequestParam("doctor.experienceYears") Integer experienceYears,
                           @RequestParam("doctor.roomId") int roomId,
                           @RequestParam("patient.fullName") String fullNamePatient,
                           @RequestParam("patient.email") String emailPatient,
                           @RequestParam("patient.phoneNumber") String phoneNumberPatient,
                           @RequestParam("patient.gender") String genderPatient,
                           @RequestParam("patient.dateOfBirth") String dateOfBirthPatient,
                           @RequestParam("patient.address") String address,
                           @RequestParam("patient.medicalHistory") String medicalHistory){
        UsersDTO usersDTO = new UsersDTO(username,password,roleName);
        DoctorDTO doctorDTO = new DoctorDTO(fullName,email,phoneNumber,gender,dateOfBirth,specialization,experienceYears,roomId);
        PatientDTO patientDTO = new PatientDTO(fullNamePatient,emailPatient,phoneNumberPatient,genderPatient,dateOfBirthPatient,address,medicalHistory);
        // Gọi service để thêm người dùng và bệnh nhân hoặc bác sĩ
        userService.addUser(usersDTO, doctorDTO, patientDTO);
        System.err.println(username);
        // Sau khi thêm thành công, chuyển hướng đến trang danh sách người dùng hoặc trang khác
        return "redirect:/admin/user"; // Ví dụ điều hướng đến trang danh sách người dùng
    }


    @GetMapping
    public String listUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // Đảm bảo giá trị page luôn >= 1
        page = Math.max(page, 1);

        // Tạo Pageable từ page và size
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách người dùng chưa bị xóa
        Page<UsersDTO> userPage = userService.findAllActiveUsers(pageable);

        // Đưa các thông tin cần thiết vào model
        model.addAttribute("users", userPage); // Danh sách người dùng
        model.addAttribute("currentPage", userPage.getNumber() + 1); // Trang hiện tại
        model.addAttribute("totalItems", userPage.getTotalElements()); // Tổng số phần tử
        model.addAttribute("totalPages", userPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", userPage.getSize()); // Kích thước trang

        // Trả về trang view tương ứng (ví dụ: admin/user.html)
        return "admin/user";
    }


    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "type", defaultValue = "username") String type,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // Điều chỉnh giá trị page để luôn >= 1
        page = Math.max(page, 1);

        // Giảm `page` đi 1 vì `Pageable` sử dụng chỉ số bắt đầu từ 0
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("username").ascending());

        Page<UsersDTO> userPage;

        // Thực hiện tìm kiếm dựa trên từ khóa và loại tìm kiếm
        if (query != null && !query.isEmpty()) {
            switch (type) {
                case "username":
                    userPage = userService.searchUsersByNameWithPagination(query, pageable);
                    break;
                default:
                    userPage = userService.findAllActiveUsers(pageable);
            }
        } else {
            userPage = userService.findAllActiveUsers(pageable);
        }

        // Thêm các thuộc tính cần thiết vào model để hiển thị trong giao diện
        model.addAttribute("users", userPage);
        model.addAttribute("currentPage", userPage.getNumber() + 1);
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("pageSize", userPage.getSize());
        model.addAttribute("searchKeyword", query);
        model.addAttribute("type", type);

        // Trả về tên file HTML (trang hiển thị danh sách bác sĩ)
        return "admin/user";
    }

    // Xóa bác sĩ
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<List<UsersDTO>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        // Trả về danh sách bác sĩ chưa bị xóa
        List<UsersDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

}
