package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class UsersDTO {
    private Long userId;
    private String username;
    private String Password;
    private String roleName;

    // Thêm thuộc tính DoctorDTO và PatientDTO
    private DoctorDTO doctorDTO; // Chỉ sử dụng khi vai trò là ROLE_DOCTOR
    private PatientDTO patientDTO; // Chỉ sử dụng khi vai trò là ROLE_PATIENT
    public UsersDTO(){}
    public UsersDTO(String username, String password, String roleName) {
        this.username = username;
        this.Password = password;
        this.roleName = roleName;
    }
}
