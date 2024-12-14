package com.example.clinicweb.controller;

import com.example.clinicweb.service.CloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    private CloudStorageService cloudStorageService;

    // Hiển thị trang upload
    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }

    // Xử lý yêu cầu upload file
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            String fileUrl = cloudStorageService.uploadFile(file);
            redirectAttributes.addFlashAttribute("message", "Tải lên thành công!");
            redirectAttributes.addFlashAttribute("fileUrl", fileUrl);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải lên: " + e.getMessage());
        }
        return "redirect:/upload";
    }
}
