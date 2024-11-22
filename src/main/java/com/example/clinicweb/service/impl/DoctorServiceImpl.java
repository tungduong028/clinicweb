package com.example.clinicweb.service.impl;

import com.example.clinicweb.model.Doctor;
import com.example.clinicweb.repository.DoctorRepository;
import com.example.clinicweb.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    // Thêm bác sĩ mới
    @Override
    public String saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
        return "success";
    }

    // Lấy bác sĩ theo ID
    @Override
    public Doctor getDoctorById(Long id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        return optionalDoctor.orElseThrow(() -> new RuntimeException("Doctor with ID " + id + " does not exist."));
    }

    // Cập nhật thông tin bác sĩ
    @Override
    public void updateDoctor(Doctor doctor) {
        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(doctor.getDoctorId());
        if (existingDoctorOpt.isPresent()) {
            Doctor existingDoctor = existingDoctorOpt.get();

            // Cập nhật thông tin từ đối tượng mới
            existingDoctor.setFullName(doctor.getFullName());
            existingDoctor.setEmail(doctor.getEmail());
            existingDoctor.setSpecialization(doctor.getSpecialization());
            existingDoctor.setExperienceYears(doctor.getExperienceYears());

            // Lưu thay đổi vào cơ sở dữ liệu
            doctorRepository.save(existingDoctor);
        } else {
            throw new RuntimeException("Doctor with ID " + doctor.getDoctorId() + " does not exist.");
        }
    }

    // Xóa bác sĩ
    @Override
    public void deleteDoctor(Long id) {
        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isPresent()) {
            doctorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Doctor with ID " + id + " does not exist.");
        }
    }


    public List<Doctor> searchDoctors(String keyword) {
        return doctorRepository.searchDoctors(keyword);
    }
}
