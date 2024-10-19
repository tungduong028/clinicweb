package com.example.clinicweb.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Long appointmentId;
    private LocalDateTime paymentDate;
    private double amount;
    private String paymentMethod;
    private String status;
}

