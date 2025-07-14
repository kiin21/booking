package org.example.booking.domain.booking.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingStatus {

    PROCESSING(0),
    BOOKED(1),
    COMPLETED(2),
    CANCELLED(3);

    private final int value;
}
