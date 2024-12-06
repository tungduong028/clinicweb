package com.example.clinicweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String specialization;
    private int experienceYears;

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

    @Column(nullable = true)
    private String imageUrl;

    private boolean isDeleted = false;
}
