package com.example.clinicweb.service;

import com.example.clinicweb.dto.ServiceDTO;
import com.example.clinicweb.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ServiceService {

    void saveService(ServiceDTO service);

    Page<Service> findAll(Pageable pageable);

    Optional<Service> findById(Long id);

    int markAsDeleted(Long id);

    Page<Service> findByIsDeletedFalse(Pageable pageable);

    Page<Service> findByIsDeletedFalseAndServiceNameContainingIgnoreCase(String keyword, Pageable pageable);
}
