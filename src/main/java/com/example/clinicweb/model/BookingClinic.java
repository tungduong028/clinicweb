package com.example.clinicweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "booking_clinic")
public class BookingClinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate bookingDate;
    private LocalTime bookingTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.SCHEDULED;

    private String notes;

    @ManyToMany
    @JoinTable(
            name = "booking_clinic_services",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services;

    public enum Status {
        SCHEDULED, COMPLETED, CANCELLED
    }
}
