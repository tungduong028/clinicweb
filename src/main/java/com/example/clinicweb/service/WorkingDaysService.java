package com.example.clinicweb.service;

import com.example.clinicweb.model.Appointment;
import com.example.clinicweb.model.WorkingDays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkingDaysService {
    public List<WorkingDays> findAll();
    public WorkingDays save(WorkingDays workingDays);
    public WorkingDays findById(Long id);
    public void deleteById(Long id);
    public Page<WorkingDays> findAll(Pageable pageable);
    public Page<WorkingDays> findByDoctor_DoctorId(Long id, Pageable pageable);
}
