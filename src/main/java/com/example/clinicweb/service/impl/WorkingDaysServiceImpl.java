package com.example.clinicweb.service.impl;

import com.example.clinicweb.model.Appointment;
import com.example.clinicweb.model.WorkingDays;
import com.example.clinicweb.repository.WorkingDaysRepository;
import com.example.clinicweb.service.WorkingDaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkingDaysServiceImpl implements WorkingDaysService {
    @Autowired
    private WorkingDaysRepository workingDaysRepository;

    public List<WorkingDays> findAll() {
        return workingDaysRepository.findAll();
    }

    public WorkingDays findById(Integer id) {
        return workingDaysRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy ngày làm việc!"));
    }

    public WorkingDays save(WorkingDays workingDays) {
        workingDaysRepository.save(workingDays);
        return workingDays;
    }

    @Override
    public WorkingDays findById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }


    public void delete(Integer id) {
        workingDaysRepository.deleteById(id);
    }
    public Page<WorkingDays> findByDoctor_DoctorId(Long id, Pageable pageable){ return workingDaysRepository.findByDoctor_DoctorId(id, pageable);}
    public Page<WorkingDays> findByIsDeletedFalse(Pageable pageable){ return workingDaysRepository.findAll(pageable);}
    public Page<WorkingDays> findAll(Pageable pageable){ return workingDaysRepository.findAll(pageable);}
//    public Optional<WorkingDays> findById(Long id){ return workingDaysRepository.findById(id);}
}
