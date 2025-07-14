package org.example.booking.domain.booking.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.booking.domain.booking.mapper.BookingMapper;
import org.example.booking.domain.booking.repository.BookingRepository;
import org.example.booking.domain.booking.repository.HomestayAvailabilityRepository;
import org.example.booking.domain.booking.service.AvailabilityService;
import org.example.booking.domain.booking.service.PricingService;
import org.example.booking.domain.homestay.service.HomestayService;
import org.example.booking.domain.payment.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VillaBookingService extends BaseBookingService {

    public VillaBookingService(
            BookingRepository bookingRepository,
            HomestayAvailabilityRepository availabilityRepository,
            AvailabilityService availabilityService,
            HomestayService homestayService,
            PricingService pricingService,
            BookingMapper mapper,
            PaymentService paymentService) {
        super(bookingRepository, availabilityRepository, availabilityService, homestayService, pricingService, mapper, paymentService);
    }
}
