package com.example.clinicweb.service;

import com.example.clinicweb.dto.ServiceDTO;
import com.example.clinicweb.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<Service> getAllService(Integer pageNo, Integer pageSize, String sortBy);

    void saveService(ServiceDTO service);

    Page<Service> findByServiceNameLikeIgnoreCase(String name, Pageable pageable);

    Page<Service> findByOrderByPriceAsc(double price, Pageable pageable);

    Page<Service> findAll(Pageable pageable);

    void deleteById(Long id);

    Optional<Service> findById(Long id);
}
