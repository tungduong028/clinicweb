package com.example.clinicweb.controller;

import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class PatientController {
    @Autowired
    PatientService patientService;

    @Autowired
    UserService userService;

    @GetMapping("/admin/patient")
    public String getAll(Model model, @RequestParam(required = false) String keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            List<Patient> patients;
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Patient> pageTuts;
            if (keyword == null) {
                pageTuts = patientService.findAll(paging);
            } else {
                pageTuts = patientService.findByIsDeletedFalseAndFullNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }
//            pageTuts = patientService.findAll(paging);

            patients = pageTuts.getContent();

            model.addAttribute("patients", patients);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "admin/patient";
    }

    @GetMapping("/admin/patient/new")
    public String newPatient(Model model) {
        PatientDTO patient = new PatientDTO();

        model.addAttribute("patient", patient);
        model.addAttribute("users", userService.findByRolePatient());
        model.addAttribute("pageTitle", "Tạo bệnh nhân mới");

        return "admin/patient/patient_form";
    }

    @PostMapping("/admin/patient/save")
    public String savePatient(
            @ModelAttribute("user") UsersDTO usersDTO,
            @ModelAttribute("patient") PatientDTO patientDTO) {

        // Gọi service để đăng ký người dùng và bệnh nhân
        userService.register(usersDTO, patientDTO);

        // Sau khi đăng ký thành công, chuyển hướng đến trang login
        return "redirect:/admin/patient";
    }

    @GetMapping("/admin/patient/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Patient patient = patientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));

        model.addAttribute("patient", patient);
        model.addAttribute("users", userService.findByRolePatient());
        model.addAttribute("pageTitle", "Sửa thông tin bệnh nhân");
        return "admin/patient/patient_form_edit";
    }
    @PostMapping("/admin/patient/update")
    public String updatePatient(@ModelAttribute("patient") PatientDTO patientDto,
                                @RequestParam("id") Long id,
                                @ModelAttribute("password") String password,
                                RedirectAttributes redirectAttributes) {
        Patient patient = patientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));
            patientService.savePatient(patient, patientDto);
            redirectAttributes.addFlashAttribute("message", "Cập nhật bệnh nhân thành công!");
        return "redirect:/admin/patient";
    }

    @GetMapping("/admin/patient/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            patientService.markAsDeleted(id);

            redirectAttributes.addFlashAttribute("message", "The Patient with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/admin/patient";
    }

    @GetMapping("/admin/patient/findOne/{id}")
    @ResponseBody
    public Patient findOne(@PathVariable("id") Long id){
        return patientService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Paitient not found with ID: " + id));
    }
}
