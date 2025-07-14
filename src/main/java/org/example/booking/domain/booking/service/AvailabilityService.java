package org.example.booking.domain.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.domain.booking.constant.AvailabilityStatus;
import org.example.booking.domain.booking.entity.HomestayAvailability;
import org.example.booking.domain.booking.repository.HomestayAvailabilityRepository;
import org.example.booking.domain.common.constant.ResponseCode;
import org.example.booking.domain.common.exception.BusinessException;
import org.example.booking.infrastructure.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvailabilityService {
    private static final int NIGHT_MAX = 365;

    private final HomestayAvailabilityRepository availabilityRepository;

    public List<HomestayAvailability> checkAvailabilityForBooking(final Long homestayId,
                                                                  final LocalDate checkinDate,
                                                                  final LocalDate checkoutDate) {

        final int nights = (int) DateUtil.getDiffInDays(checkinDate, checkoutDate);
        if (nights > NIGHT_MAX) {
            throw new BusinessException(ResponseCode.NIGHTS_INVALID);
        }

        final List<HomestayAvailability> availableDays = availabilityRepository.findAndLockHomestayAvailability(
                homestayId,
                AvailabilityStatus.AVAILABLE.getValue(),
                checkinDate,
                checkoutDate
        );
        if (availableDays.isEmpty() || availableDays.size() < nights) {
            throw new BusinessException(ResponseCode.HOMESTAY_BUSY);
        }

        return availableDays;
    }


    @Transactional
    public void saveAll(List<HomestayAvailability> availableDays) {
        availabilityRepository.saveAll(availableDays);
    }
}
