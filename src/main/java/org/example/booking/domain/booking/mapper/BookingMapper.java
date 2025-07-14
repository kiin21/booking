package org.example.booking.domain.booking.mapper;

import org.example.booking.domain.booking.dto.response.BookingResponse;
import org.example.booking.domain.booking.dto.response.BookingStatusResponse;
import org.example.booking.domain.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "id", target = "bookingId")
    BookingResponse.Booking toBookingDTO(Booking booking);

    @Mapping(source = "id", target = "bookingId")
    BookingStatusResponse toBookingStatusResponse(Booking booking);
}
