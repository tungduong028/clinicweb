package com.example.clinicweb.service;

import com.example.clinicweb.model.WorkingTimes;

public interface WorkingTimesService {
    WorkingTimes findByDoctor_DoctorId(Long doctorId);
}
