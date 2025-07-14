package org.example.booking.domain.booking.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingRequest {

    @NotBlank(message = "message cannot be blank")
    private String requestId;

    @NotNull(message = "user_id cannot be null")
    private String userId;

    @NotNull(message = "homestay_id cannot be blank")
    private String homestayId;

    @NotNull(message = "checkin_date cannot be blank")
    private LocalDate checkinDate;

    @NotNull(message = "checkout_date cannot be blank")
    private LocalDate checkoutDate;

    @Positive(message = "guests must be positive")
    private Integer guests;

    @Length(max = 500, message = "note cannot be longer than 500 characters")
    private String note;

    private String ipAddress;
}
