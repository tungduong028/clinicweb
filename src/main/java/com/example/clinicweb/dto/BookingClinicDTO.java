package com.example.clinicweb.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingClinicDTO {

    private Long bookingId;
    private Long patientId;
    private Long doctorId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private String status;
    private String notes;
    private List<Long> serviceIds;
}
