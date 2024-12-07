package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class ServiceDTO {
    private Long serviceId;
    private String serviceName;
    private String description;
    private double price;
    private String imageUrl;
    private boolean isDeleted = false;
}

