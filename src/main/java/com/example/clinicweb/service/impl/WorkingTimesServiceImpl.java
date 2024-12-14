package com.example.clinicweb.service.impl;

import com.example.clinicweb.model.WorkingTimes;
import com.example.clinicweb.service.WorkingTimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.clinicweb.repository.WorkingTimesRepository;

@Service
public class WorkingTimesServiceImpl implements WorkingTimesService {
    @Autowired
    private WorkingTimesRepository workingTimesRepository;

    @Override
    public WorkingTimes findByDoctor_DoctorId(Long doctorId) {
        return workingTimesRepository.findByDoctor_DoctorId(doctorId);
    }
}

