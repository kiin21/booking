package org.example.booking.domain.booking.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.example.booking.domain.payment.dto.response.InitPaymentResponse;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingResponse {

    private Booking booking;
    private InitPaymentResponse payment;

    @Builder
    @Getter
    public static class Booking {
        private String bookingId;
        private String userId;
        private String homestayId;
        private String checkinDate;
        private String checkoutDate;
        private Integer guests;
        private Integer status;
        private BigDecimal subtotal;
        private BigDecimal discount;
        private BigDecimal totalAmount;
        private String currency;
        private String requestId;
        private String note;
    }
}
