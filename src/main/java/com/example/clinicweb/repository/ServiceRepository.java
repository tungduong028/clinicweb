package com.example.clinicweb.repository;

import com.example.clinicweb.model.Service;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceRepository extends PagingAndSortingRepository<Service, Long>, JpaRepository<Service, Long> {
    Page<Service> findByIsDeletedFalse(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Service s SET s.isDeleted = true WHERE s.serviceId = :id AND s.isDeleted = false")
    int markAsDeleted(Long id);

    Page<Service> findByIsDeletedFalseAndServiceNameContainingIgnoreCase(String name, Pageable pageable);
}
