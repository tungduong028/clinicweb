package com.example.clinicweb.dto;

import com.example.clinicweb.model.Doctor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class DoctorDTO {
    private Long doctorId;          // ID của bác sĩ, có thể dùng để sửa hoặc xóa bác sĩ
    private Long userId;            // ID của người dùng (user_id) để liên kết với bảng user
    private String specialization;  // Chuyên môn của bác sĩ
    private Integer experienceYears;    // Số năm kinh nghiệm của bác sĩ
    private int roomId;             // ID của phòng khám mà bác sĩ làm việc

    private String fullName;        // Tên đầy đủ của bác sĩ
    private String email;           // Địa chỉ email của bác sĩ
    private String phoneNumber;     // Số điện thoại của bác sĩ
    private String gender;          // Giới tính của bác sĩ
    private String dateOfBirth;  // Ngày sinh của bác sĩ (dùng LocalDate thay vì String)
    public DoctorDTO(){}
    public DoctorDTO(String fullName, String email, String phoneNumber, String gender, String dateOfBirth, String specialization, Integer experienceYears, int roomId) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.roomId = roomId;
    }
}

