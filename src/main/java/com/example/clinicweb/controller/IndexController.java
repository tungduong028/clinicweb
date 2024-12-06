package com.example.clinicweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");
        return "about";
    }
//    @GetMapping("/service")
//    public String service(Model model) {
//        model.addAttribute("title", "service");
//        return "service";
//    }
    @GetMapping("/team")
    public String team(Model model) {
        model.addAttribute("title", "team");
        return "team";
    }
    @GetMapping("/appointment")
    public String appointment(Model model) {
        model.addAttribute("title", "appointment");
        return "appointment";
    }
    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("title", "search");
        return "search";
    }
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "contact");
        return "contact";
    }

}
