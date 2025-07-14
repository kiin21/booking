package org.example.booking.domain.booking.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.booking.domain.booking.constant.BookingStatus;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusResponse {
    @JsonProperty("booking_id")
    private String bookingId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("homestay_id")
    private String homestayId;

    @JsonProperty("status")
    private BookingStatus status;
}
