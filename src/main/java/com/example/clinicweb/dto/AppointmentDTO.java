package com.example.clinicweb.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentDTO {
    private Long doctorId;
    private Long patientId;
    private LocalDate date;
    private String time;
}
