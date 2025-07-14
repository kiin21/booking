package org.example.booking.domain.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.domain.booking.constant.Currency;
import org.example.booking.domain.booking.dto.BookingPrice;
import org.example.booking.domain.booking.entity.HomestayAvailability;
import org.example.booking.domain.homestay.entity.Homestay;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PricingService {

    private final DiscountService discountService;

    public BookingPrice calculate(final List<HomestayAvailability> availableDays) {
        final int nights = availableDays.size();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (HomestayAvailability aDay : availableDays) {
            subtotal = subtotal.add(aDay.getPrice());
        }

        final BigDecimal discount = discountService.getDiscountAmount(subtotal, nights);

        return BookingPrice.builder()
                .subtotal(subtotal)
                .discount(discount)
                .totalAmount(subtotal.add(discount))
                .currency(Currency.USD.getValue())
                .build();
    }

    public BookingPrice calculate(Homestay homestay, List<HomestayAvailability> availableDays) {
        final int nights = availableDays.size();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (HomestayAvailability aDay : availableDays) {
            subtotal = subtotal.add(aDay.getPrice());
        }

        final BigDecimal discount = discountService.getDiscountAmount(subtotal, nights);

        return BookingPrice.builder()
                .subtotal(subtotal)
                .discount(discount)
                .totalAmount(subtotal.add(discount))
                .currency(Currency.USD.getValue())
                .build();
    }
}
