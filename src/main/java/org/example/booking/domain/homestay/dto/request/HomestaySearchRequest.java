package org.example.booking.domain.homestay.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.booking.domain.booking.constant.AvailabilityStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestaySearchRequest {

    private Double longitude;
    private Double latitude;
    private Double radius;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer guests;
    private AvailabilityStatus status;

}