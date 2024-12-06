package com.example.clinicweb.controller;

import com.example.clinicweb.dto.ServiceDTO;
import com.example.clinicweb.model.Service;
import com.example.clinicweb.repository.ServiceRepository;
import com.example.clinicweb.service.CloudStorageService;
import com.example.clinicweb.service.impl.ServiceServiceImpl;
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
public class ServiceController {
    @Autowired
    ServiceServiceImpl serviceService;
    @Autowired
    private CloudStorageService cloudStorageService;

    @GetMapping("/service")
    public String showService(Model model, @RequestParam(required = false) String keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "6") int size) {
        try {
            List<Service> services = new ArrayList<Service>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Service> pageTuts;
//            if (keyword == null) {
//                pageTuts = serviceService.findAll(paging);
//            } else {
//                pageTuts = serviceService.findByServiceNameLikeIgnoreCase(keyword, paging);
//                model.addAttribute("keyword", keyword);
//            }
//            pageTuts = serviceService.findAll(paging);
            pageTuts = serviceService.findByIsDeletedFalse(paging);
            services = pageTuts.getContent();

            model.addAttribute("services", services);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            //model.addAttribute("message", e.getMessage());
        }
        return "service";
    }

    @GetMapping("/admin/service")
    public String getAll(Model model, @RequestParam(required = false) String keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            List<Service> services = new ArrayList<Service>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Service> pageTuts;
            if (keyword == null) {
                pageTuts = serviceService.findAll(paging);
            } else {
                pageTuts = serviceService.findByServiceNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }
            services = pageTuts.getContent();

            model.addAttribute("services", services);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "admin/service";
    }

    @GetMapping("/admin/service/new")
    public String newService(Model model) {
        Service service = new Service();

        model.addAttribute("service", service);
        model.addAttribute("pageTitle", "Tạo dịch vụ mới");

        return "admin/service/service_form";
    }

    @PostMapping("/admin/service/save")
    public String saveService(@RequestParam("file") MultipartFile file, @ModelAttribute("service")ServiceDTO serviceDTO) {
        String fileUrl = null;
        try {
            fileUrl = cloudStorageService.uploadFile(file);
//            redirectAttributes.addFlashAttribute("message", "Tải lên thành công!");
//            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
        } catch (IOException e) {
//            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên: " + e.getMessage());
        }
        serviceDTO.setImageUrl(fileUrl);
        serviceService.saveService(serviceDTO);
        return "redirect:/admin/service";
    }

    @GetMapping("/admin/service/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Service service = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid service Id:" + id));

        model.addAttribute("service", service);
        model.addAttribute("imageUrl", service.getImageUrl());
        model.addAttribute("pageTitle", "Sửa thông tin dịch vụ");
        return "admin/service/service_form";
    }

    @GetMapping("/admin/service/delete/{id}")
    public String deleteService(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            serviceService.markAsDeleted(id);

            redirectAttributes.addFlashAttribute("message", "The Service with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/admin/service";
    }

    @PostMapping("/admin/service/upload/{id}")
    public String handleFileUpload(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            String fileUrl = cloudStorageService.uploadFile(file);
            redirectAttributes.addFlashAttribute("message", "Tải lên thành công!");
            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên: " + e.getMessage());
        }
        return "redirect:/admin/service";
    }

    @GetMapping("/admin/service/findOne/{id}")
    @ResponseBody
    public Service findOne(@PathVariable("id") Long id){
        return serviceService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Service not found with ID: " + id));
    }
}
