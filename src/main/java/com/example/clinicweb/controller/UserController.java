package com.example.clinicweb.controller;

import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.service.CloudStorageService;
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
import com.example.clinicweb.dto.UsersDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    private CloudStorageService cloudStorageService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/admin/user")
    public String getAll(Model model, @RequestParam(required = false) String keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            List<Users> users = new ArrayList<Users>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Users> pageTuts;
            if (keyword == null) {
                pageTuts = userService.findAll(paging);
            } else {
                pageTuts = userService.findByUsernameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }
            users = pageTuts.getContent();

            model.addAttribute("users", users);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "admin/user";
    }

    @GetMapping("/admin/user/new")
    public String newUsers(Model model) {
        Users user = new Users();

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Tạo tài khoản mới");

        return "admin/user/user_form";
    }

    @PostMapping("/admin/user/save")
    public String saveUsers( @ModelAttribute("user") UsersDTO userDTO) {
//        String fileUrl = null;
//        try {
//            fileUrl = cloudStorageService.uploadFile(file);
////            redirectAttributes.addFlashAttribute("message", "Tải lên thành công!");
////            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
//        } catch (IOException e) {
////            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên: " + e.getMessage());
//        }
//        userDTO.setImageUrl(fileUrl);
        System.out.println("GGGGGGG"+userDTO.getRoleName());
        userService.saveUser(userDTO);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Users user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("pageTitle", "Sửa thông tin tài khoản");
        return "admin/user/user_form";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String deleteUsers(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.markAsDeleted(id);

            redirectAttributes.addFlashAttribute("message", "The Users with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/admin/user";
    }

//    @PostMapping("/admin/user/upload/{id}")
//    public String handleFileUpload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file,
//                                   RedirectAttributes redirectAttributes) {
//        try {
//            String fileUrl = cloudStorageService.uploadFile(file);
//            redirectAttributes.addFlashAttribute("message", "Tải lên thành công!");
//            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
//        } catch (IOException e) {
//            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên: " + e.getMessage());
//        }
//        return "redirect:/admin/user";
//    }
}
