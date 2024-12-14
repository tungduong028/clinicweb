package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.ServiceDTO;
import com.example.clinicweb.model.Service;
import com.example.clinicweb.repository.ServiceRepository;
import com.example.clinicweb.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public void saveService(ServiceDTO service) {
        Service service1 = new Service();
        service1.setServiceName(service.getServiceName());
        service1.setServiceId(service.getServiceId());
        service1.setPrice(service.getPrice());
        service1.setDescription(service.getDescription());
        service1.setImageUrl(service.getImageUrl());
        serviceRepository.save(service1);
    }

    @Override
    public Page<Service> findAll(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    @Override
    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Page<Service> findByIsDeletedFalseAndServiceNameContainingIgnoreCase(String keyword, Pageable pageable){
        return serviceRepository.findByIsDeletedFalseAndServiceNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public int markAsDeleted(Long id) {
        return serviceRepository.markAsDeleted(id);
    }

    @Override
    public  Page<Service> findByIsDeletedFalse(Pageable pageable){
        return serviceRepository.findByIsDeletedFalse(pageable);
    }
}
