package com.example.clinicweb.repository;

import com.example.clinicweb.model.Service;
import com.example.clinicweb.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends PagingAndSortingRepository<Service, Long>, JpaRepository<Service, Long> {
    Page<Service> findByServiceNameLikeIgnoreCase(String name, Pageable pageable);

    Page<Service> findByOrderByPriceAsc(double price, Pageable pageable);
}
