package com.example.clinicweb.controller;

import com.example.clinicweb.dto.BookingClinicDTO;
import com.example.clinicweb.service.BookingClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingClinicController {

    @Autowired
    private BookingClinicService bookingClinicService;

    // Tạo mới một lịch hẹn
    @PostMapping
    public ResponseEntity<BookingClinicDTO> createBooking(@RequestBody BookingClinicDTO bookingClinicDTO) {
        BookingClinicDTO createdBooking = bookingClinicService.createBooking(bookingClinicDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    // Cập nhật lịch hẹn dựa trên bookingId
    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingClinicDTO> updateBooking(@PathVariable Long bookingId, @RequestBody BookingClinicDTO bookingClinicDTO) {
        BookingClinicDTO updatedBooking = bookingClinicService.updateBooking(bookingId, bookingClinicDTO);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    // Lấy thông tin lịch hẹn theo bookingId
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingClinicDTO> getBookingById(@PathVariable Long bookingId) {
        BookingClinicDTO booking = bookingClinicService.getBookingById(bookingId);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    // Lấy danh sách tất cả các lịch hẹn
    @GetMapping
    public ResponseEntity<List<BookingClinicDTO>> getAllBookings() {
        List<BookingClinicDTO> bookings = bookingClinicService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    // Xóa một lịch hẹn dựa trên bookingId
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        bookingClinicService.deleteBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
