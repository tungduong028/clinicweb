package com.example.clinicweb.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class AppointmentDTO {
    private Long doctorId;
    private Long patientId;
    private Date date;
    private String time;
}
