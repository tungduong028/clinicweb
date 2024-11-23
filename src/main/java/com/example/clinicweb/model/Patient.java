package com.example.clinicweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Users user;

    private String medicalHistory;
    private String address;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String dateOfBirth;
}

