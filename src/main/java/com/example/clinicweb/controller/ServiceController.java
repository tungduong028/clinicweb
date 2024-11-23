package com.example.clinicweb.controller;

import com.example.clinicweb.dto.ServiceDTO;
import com.example.clinicweb.model.Service;
import com.example.clinicweb.service.impl.ServiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ServiceController {
    @Autowired
    ServiceServiceImpl serviceService;

    @GetMapping("/admin/service")
    public String getAll(Model model, @RequestParam(required = false) String keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
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
            pageTuts = serviceService.findAll(paging);

            services = pageTuts.getContent();

            model.addAttribute("services", services);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            //model.addAttribute("message", e.getMessage());
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
    public String saveService(@ModelAttribute("service")ServiceDTO serviceDTO) {
        serviceService.saveService(serviceDTO);
        return "redirect:/admin/service";
    }

    @GetMapping("/admin/service/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Service service = serviceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid service Id:" + id));

        model.addAttribute("service", service);
        model.addAttribute("pageTitle", "Sửa thông tin dịch vụ");
        return "admin/service/service_form";
    }

    @GetMapping("/admin/service/delete/{id}")
    public String deleteService(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            serviceService.deleteById(id);

            redirectAttributes.addFlashAttribute("message", "The Service with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/admin/service";
    }
}
