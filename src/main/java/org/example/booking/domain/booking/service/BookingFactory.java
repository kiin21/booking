package org.example.booking.domain.booking.service;

import lombok.RequiredArgsConstructor;
import org.example.booking.domain.booking.dto.request.BookingRequest;
import org.example.booking.domain.booking.service.impl.ApartmentBookingService;
import org.example.booking.domain.booking.service.impl.BaseBookingService;
import org.example.booking.domain.booking.service.impl.VillaBookingService;
import org.example.booking.domain.common.constant.ResponseCode;
import org.example.booking.domain.common.exception.BusinessException;
import org.example.booking.domain.homestay.entity.Homestay;
import org.example.booking.domain.homestay.entity.ListingType;
import org.example.booking.domain.homestay.service.HomestayService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingFactory {   // Abstract Factory

    private final VillaBookingService villaBookingService;
    private final HomestayService homestayService;
    private final BaseBookingService baseBookingService;
    private final ApartmentBookingService apartmentBookingService;


    public BookingService build(BookingRequest request) {
        Homestay homestay = homestayService.getHomestayById(Long.valueOf(request.getHomestayId()));
        if (homestay == null) {
            throw new BusinessException(ResponseCode.HOMESTAY_NOT_FOUND);
        }

        return switch (ListingType.of(homestay.getType())) {
            case APARTMENT -> apartmentBookingService;
            case VILLA -> villaBookingService;
            default -> baseBookingService;
        };
    }

    public BookingService build() {
        return baseBookingService;
    }
}
