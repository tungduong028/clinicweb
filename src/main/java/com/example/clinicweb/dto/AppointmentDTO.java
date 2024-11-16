package com.example.clinicweb.dto;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
    private LocalTime time;
}
