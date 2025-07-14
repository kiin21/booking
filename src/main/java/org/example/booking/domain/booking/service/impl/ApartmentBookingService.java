package org.example.booking.domain.booking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.booking.domain.booking.dto.request.BookingRequest;
import org.example.booking.domain.booking.entity.Booking;
import org.example.booking.domain.booking.entity.HomestayAvailability;
import org.example.booking.domain.booking.mapper.BookingMapper;
import org.example.booking.domain.booking.repository.BookingRepository;
import org.example.booking.domain.booking.repository.HomestayAvailabilityRepository;
import org.example.booking.domain.booking.service.AvailabilityService;
import org.example.booking.domain.booking.service.PricingService;
import org.example.booking.domain.homestay.entity.Homestay;
import org.example.booking.domain.homestay.service.HomestayService;
import org.example.booking.domain.payment.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ApartmentBookingService extends BaseBookingService {

    public ApartmentBookingService(
            BookingRepository bookingRepository,
            HomestayAvailabilityRepository availabilityRepository,
            AvailabilityService availabilityService,
            HomestayService homestayService,
            PricingService pricingService,
            BookingMapper mapper,
            PaymentService paymentService) {
        super(bookingRepository, availabilityRepository, availabilityService, homestayService, pricingService, mapper, paymentService);
    }

    @Override
    protected List<HomestayAvailability> checkAvailability(Homestay homestay, BookingRequest request) {
        log.info("Checking the status of the building...");
        return super.checkAvailability(homestay, request);
    }

    @Override
    protected void sendNotifications(Booking booking) {
        super.sendNotifications(booking);
        log.info("Sending email to the building...");
    }

    @Override
    protected void postProcess(Booking booking) {
        log.info("Producing event to topic: {}, value: {}", "booking-dev", booking.getId());
    }
}
