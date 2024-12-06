package com.example.clinicweb.dto;

import lombok.Data;

@Data
public class UsersDTO {
    private Long userId;
    private String username;
    private String passwordHash;
    private String roleName;
    private boolean isDeleted=false;
}
