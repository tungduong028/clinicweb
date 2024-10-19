package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Long patientId;
    private Long userId;
    private String address;
    private String medicalHistory;
}
