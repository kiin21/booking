package org.example.booking.domain.booking.service;

import org.example.booking.domain.booking.dto.request.BookingRequest;
import org.example.booking.domain.booking.dto.response.BookingResponse;
import org.example.booking.domain.booking.dto.response.BookingStatusResponse;

public interface BookingService {
    BookingResponse book(BookingRequest request);

    void markAsBooked(Long bookingId);

    BookingStatusResponse getBookingStatus(Long bookingId);
}
