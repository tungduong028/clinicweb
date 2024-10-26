package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long doctorId;
    private Long userId;
    private String specialization;
    private int experienceYears;
    private int roomId;

    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;
}

