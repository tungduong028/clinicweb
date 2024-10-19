package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class AppointmentServiceDTO {
    private Long id;
    private Long appointmentId;
    private Long serviceId;
    private String serviceName;
    private int quantity;
}

