package com.example.clinicweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "working_days")
public class WorkingDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workingId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

}

