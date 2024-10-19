package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class UsersDTO {
    private Long userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
    private String roleName;
}
