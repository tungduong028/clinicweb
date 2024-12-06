package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Long patientId;
    private Long userId;
    private String address;
    private String medicalHistory;

    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;

    public PatientDTO(String fullNamePatient, String emailPatient, String phoneNumberPatient, String genderPatient, String dateOfBirthPatient, String address, String medicalHistory) {
    }
}
