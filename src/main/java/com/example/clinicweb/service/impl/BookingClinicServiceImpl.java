package com.example.clinicweb.service.impl;

import com.example.clinicweb.dto.BookingClinicDTO;
import com.example.clinicweb.model.*;
import com.example.clinicweb.repository.BookingClinicRepository;
import com.example.clinicweb.service.BookingClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingClinicServiceImpl implements BookingClinicService {

    @Autowired
    private BookingClinicRepository bookingClinicRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ServiceService serviceService;

    @Override
    public BookingClinicDTO createBooking(BookingClinicDTO bookingClinicDTO) {
        BookingClinic bookingClinic = new BookingClinic();
        mapDtoToEntity(bookingClinicDTO, bookingClinic);
        bookingClinicRepository.save(bookingClinic);
        return mapEntityToDto(bookingClinic);
    }

    @Override
    public BookingClinicDTO updateBooking(Long bookingId, BookingClinicDTO bookingClinicDTO) {
        Optional<BookingClinic> optionalBookingClinic = bookingClinicRepository.findById(bookingId);
        if (optionalBookingClinic.isPresent()) {
            BookingClinic bookingClinic = optionalBookingClinic.get();
            mapDtoToEntity(bookingClinicDTO, bookingClinic);
            bookingClinicRepository.save(bookingClinic);
            return mapEntityToDto(bookingClinic);
        } else {
            throw new RuntimeException("Booking not found with id " + bookingId);
        }
    }

    @Override
    public BookingClinicDTO getBookingById(Long bookingId) {
        BookingClinic bookingClinic = bookingClinicRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id " + bookingId));
        return mapEntityToDto(bookingClinic);
    }

    @Override
    public List<BookingClinicDTO> getAllBookings() {
        return bookingClinicRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBooking(Long bookingId) {
        if (bookingClinicRepository.existsById(bookingId)) {
            bookingClinicRepository.deleteById(bookingId);
        } else {
            throw new RuntimeException("Booking not found with id " + bookingId);
        }
    }

    private void mapDtoToEntity(BookingClinicDTO bookingClinicDTO, BookingClinic bookingClinic) {
        bookingClinic.setBookingDate(bookingClinicDTO.getBookingDate());
        bookingClinic.setBookingTime(bookingClinicDTO.getBookingTime());
        bookingClinic.setStatus(BookingClinic.Status.valueOf(bookingClinicDTO.getStatus()));
        bookingClinic.setNotes(bookingClinicDTO.getNotes());

        Patient patient = patientService.getPatientById(bookingClinicDTO.getPatientId());
        bookingClinic.setPatient(patient);

        Doctor doctor = doctorService.getDoctorById(bookingClinicDTO.getDoctorId());
        bookingClinic.setDoctor(doctor);

        List<Service> services = bookingClinicDTO.getServiceIds().stream()
                .map(serviceService::getServiceById)
                .collect(Collectors.toList());
        bookingClinic.setServices(services);
    }

    private BookingClinicDTO mapEntityToDto(BookingClinic bookingClinic) {
        BookingClinicDTO bookingClinicDTO = new BookingClinicDTO();
        bookingClinicDTO.setBookingId(bookingClinic.getBookingId());
        bookingClinicDTO.setBookingDate(bookingClinic.getBookingDate());
        bookingClinicDTO.setBookingTime(bookingClinic.getBookingTime());
        bookingClinicDTO.setStatus(bookingClinic.getStatus().name());
        bookingClinicDTO.setNotes(bookingClinic.getNotes());

        bookingClinicDTO.setPatientId(bookingClinic.getPatient().getPatientId());
        bookingClinicDTO.setDoctorId(bookingClinic.getDoctor().getDoctorId());

        List<Long> serviceIds = bookingClinic.getServices().stream()
                .map(Service::getServiceId)
                .collect(Collectors.toList());
        bookingClinicDTO.setServiceIds(serviceIds);

        return bookingClinicDTO;
    }
}
