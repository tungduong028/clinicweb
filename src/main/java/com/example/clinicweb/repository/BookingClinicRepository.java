package com.example.clinicweb.repository;

import com.example.clinicweb.model.BookingClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingClinicRepository extends JpaRepository<BookingClinic, Long> {

    List<BookingClinic> findByBookingDate(LocalDate bookingDate);

    List<BookingClinic> findByBookingDateAndBookingTime(LocalDate bookingDate, LocalTime bookingTime);

    List<BookingClinic> findByStatus(BookingClinic.Status status);

    List<BookingClinic> findByDoctor_DoctorIdAndBookingDate(Long doctorId, LocalDate bookingDate);
}
