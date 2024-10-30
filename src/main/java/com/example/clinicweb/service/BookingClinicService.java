package com.example.clinicweb.service;

import com.example.clinicweb.dto.BookingClinicDTO;
import java.util.List;

public interface BookingClinicService {

    BookingClinicDTO createBooking(BookingClinicDTO bookingClinicDTO);

    BookingClinicDTO updateBooking(Long bookingId, BookingClinicDTO bookingClinicDTO);

    BookingClinicDTO getBookingById(Long bookingId);

    List<BookingClinicDTO> getAllBookings();

    void deleteBooking(Long bookingId);
}
