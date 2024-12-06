package com.example.clinicweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String specialization;
    private int experienceYears;
    private int roomId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String phoneNumber; // Đổi từ phone_number

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String dateOfBirth; // Đổi từ date_of_birth

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
