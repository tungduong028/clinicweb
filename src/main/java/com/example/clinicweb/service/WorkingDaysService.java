package com.example.clinicweb.service;

import com.example.clinicweb.model.WorkingDays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkingDaysService {
    List<WorkingDays> findAll();

    WorkingDays save(WorkingDays workingDays);

    WorkingDays findById(Long id);

    void deleteById(Long id);

    Page<WorkingDays> findAll(Pageable pageable);

    Page<WorkingDays> findByDoctor_DoctorId(Long id, Pageable pageable);
}
