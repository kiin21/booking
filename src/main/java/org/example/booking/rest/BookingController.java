package org.example.booking.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.app.dto.response.ResponseDTO;
import org.example.booking.app.service.ResponseFactory;
import org.example.booking.app.util.RequestUtil;
import org.example.booking.domain.booking.dto.request.BookingRequest;
import org.example.booking.domain.booking.dto.response.BookingResponse;
import org.example.booking.domain.booking.service.BookingFactory;
import org.example.booking.domain.booking.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final ResponseFactory responseFactory;
    private final BookingFactory bookingFactory;

    @PostMapping
    BookingResponse bookHomestay(
            @Valid @RequestBody BookingRequest request,
            HttpServletRequest httpServletRequest
    ) {
        var ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        request.setIpAddress(ipAddress);

        log.info("Booking Request: {}", request);
        BookingService bookingService = bookingFactory.build(request);
        return bookingService.book(request);
    }

    @GetMapping("/{bookingId}/status")
    ResponseDTO getBookingStatus(@PathVariable Long bookingId) {
        var service = bookingFactory.build();

        var response = service.getBookingStatus(bookingId);
        return responseFactory.response(response);
    }
}
