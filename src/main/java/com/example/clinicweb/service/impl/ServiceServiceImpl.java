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

    public List<Service> getAllService (Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Service> pagedResult = serviceRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Service>();
        }
    }

    public void saveService(ServiceDTO service) {
        Service service1 = new Service();
        service1.setServiceName(service.getServiceName());
        service1.setServiceId(service.getServiceId());
        service1.setPrice(service.getPrice());
        service1.setDescription(service.getDescription());
        serviceRepository.save(service1);
    }

    public Page<Service> findByServiceNameLikeIgnoreCase(String name, Pageable pageable) {
        return  serviceRepository.findByServiceNameLikeIgnoreCase(name, pageable);
    }

    public Page<Service> findByOrderByPriceAsc(double price, Pageable pageable) {
        return serviceRepository.findByOrderByPriceAsc(price, pageable);
    }

    public Page<Service> findAll(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
    }

    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }
}
