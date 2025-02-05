package com.example.clinicweb.controller;

import com.example.clinicweb.model.Appointment;
import com.example.clinicweb.service.WorkingDaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.clinicweb.model.WorkingDays;
import com.example.clinicweb.service.impl.*;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class WorkingDaysController {
    @Autowired
    private DoctorServiceImpl doctorService;

    @Autowired
    private WorkingDaysService workingDaysService;

    @GetMapping("/admin/workingday")
    public String getAll(Model model, @RequestParam(required = false) Long keyword
            , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            List<WorkingDays> workingDays = new ArrayList<WorkingDays>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<WorkingDays> pageTuts;
            if (keyword == null) {
                pageTuts = workingDaysService.findAll(paging);
            } else {
                pageTuts = workingDaysService.findByDoctor_DoctorId(keyword, paging);
                model.addAttribute("keyword", keyword);
            }
            workingDays = pageTuts.getContent();

            model.addAttribute("workingDays", workingDays);
            model.addAttribute("currentPage", pageTuts.getNumber() + 1);
            model.addAttribute("totalItems", pageTuts.getTotalElements());
            model.addAttribute("totalPages", pageTuts.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "admin/workingday";
    }

    @GetMapping("/admin/workingday/new")
    public String newWorkingDayForm(Model model) {
        model.addAttribute("workingDays", new WorkingDays());
        model.addAttribute("workingDays", workingDaysService.findAll());
        return "admin/workingday/workingday_form";
    }

    @PostMapping("/admin/workingday/save")
    public String saveWorkingDay(@ModelAttribute WorkingDays workingDays) {
        workingDaysService.save(workingDays);
        return "redirect:/admin/workingdays";
    }

    @GetMapping("/admin/workingday/edit/{id}")
    public String editWorkingDayForm(@PathVariable("id") Long id, Model model) {
        WorkingDays workingDay = workingDaysService.findById(id);
        model.addAttribute("workingDays", workingDay);
        model.addAttribute("doctors", doctorService.findAllDoctors());
        return "admin/workingday/workingday_form";
    }

    @PostMapping("/admin/workingday/delete/{id}")
    public String deleteWorkingDay(@PathVariable("id") Integer id) {
        workingDaysService.deleteById(Long.valueOf(id));
        return "redirect:/admin/workingday";
    }
}
