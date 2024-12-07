package com.example.clinicweb.controller;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.service.CloudStorageService;
import com.example.clinicweb.service.impl.DoctorServiceImpl;
import com.example.clinicweb.service.impl.PatientServiceImpl;
import com.example.clinicweb.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class DoctorController {
    @Autowired
    DoctorServiceImpl doctorService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private CloudStorageService cloudStorageService;

    @GetMapping("/admin/doctor")
    public String getAll(Model model, @RequestParam(required = false) String keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            List<Doctor> doctors = new ArrayList<Doctor>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Doctor> pageTuts;
            if (keyword == null) {
                pageTuts = doctorService.findAll(paging);
            } else {
                pageTuts = doctorService.findByIsDeletedFalseAndFullNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }
//            pageTuts = doctorService.findAll(paging);

            doctors = pageTuts.getContent();

            model.addAttribute("doctors", doctors);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "admin/doctor";
    }

    @GetMapping("/admin/doctor/new")
    public String newDoctor(Model model) {
        DoctorDTO doctor = new DoctorDTO();

        model.addAttribute("doctor", doctor);
        model.addAttribute("user", new UsersDTO());
        model.addAttribute("pageTitle", "Tạo bác sĩ mới");

        return "admin/doctor/doctor_form_new";
    }

    @PostMapping("/admin/doctor/save")
    public String saveDoctor(@RequestParam("file") MultipartFile file, @ModelAttribute("doctor") DoctorDTO doctorDTO) {
        String fileUrl = null;
        if (!file.isEmpty()){
            try {
                fileUrl = cloudStorageService.uploadFile(file);
//            redirectAttributes.addFlashAttribute("message", "Tải lên thành công!");
//            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
            } catch (IOException e) {
//            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên: " + e.getMessage());
            }
            doctorDTO.setImageUrl(fileUrl);
        }

        doctorService.saveDoctor(doctorDTO);
        return "redirect:/admin/doctor";
    }

    @PostMapping("/admin/doctor/save_new")
    public String saveNewDoctor(@RequestParam("file") MultipartFile file, @ModelAttribute("doctor") DoctorDTO doctorDTO, @ModelAttribute("user") UsersDTO usersDTO) {
        String fileUrl = null;
        try {
            fileUrl = cloudStorageService.uploadFile(file);
//            redirectAttributes.addFlashAttribute("message", "Tải lên thành công!");
//            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
        } catch (IOException e) {
//            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên: " + e.getMessage());
        }
        doctorDTO.setImageUrl(fileUrl);
        doctorService.saveNewDoctor(doctorDTO, usersDTO);
        return "redirect:/admin/doctor";
    }

    @GetMapping("/admin/doctor/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Doctor doctor = doctorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + id));

        DoctorDTO doctorDTO = doctorService.toDoctorDto(doctor);

        model.addAttribute("doctor", doctorDTO);
        model.addAttribute("users", userService.findByRoleDoctor());
        model.addAttribute("imageUrl", doctorDTO.getImageUrl());
        model.addAttribute("pageTitle", "Sửa thông tin bác sĩ");
        return "admin/doctor/doctor_form_edit";
    }

    @GetMapping("/admin/doctor/delete/{id}")
    public String deleteDoctor(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            doctorService.markAsDeleted(id);

            redirectAttributes.addFlashAttribute("message", "The Doctor with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/admin/doctor";
    }

//    @GetMapping("/admin/doctor/findOne/{id}")
//    @ResponseBody
//    public Doctor findOne(@PathVariable("id") Long id){
//        return doctorService.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Paitient not found with ID: " + id));
//    }
}
