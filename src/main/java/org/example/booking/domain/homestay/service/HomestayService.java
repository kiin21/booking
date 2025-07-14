package org.example.booking.domain.homestay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.domain.booking.constant.AvailabilityStatus;
import org.example.booking.domain.common.constant.ResponseCode;
import org.example.booking.domain.common.exception.BusinessException;
import org.example.booking.domain.homestay.dto.HomestayDTO;
import org.example.booking.domain.homestay.dto.request.HomestaySearchRequest;
import org.example.booking.domain.homestay.entity.Homestay;
import org.example.booking.domain.homestay.repository.HomestayRepository;
import org.example.booking.infrastructure.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomestayService {

    private final HomestayRepository repository;

    public Homestay getHomestayById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<HomestayDTO> searchHomestays(HomestaySearchRequest request) {
        request.setStatus(AvailabilityStatus.AVAILABLE);

        LocalDate checkinDate = request.getCheckinDate();
        LocalDate checkoutDate = request.getCheckoutDate();

        if (request.getCheckinDate().isAfter(request.getCheckoutDate())
                || request.getCheckinDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ResponseCode.CHECKIN_DATE_INVALID);
        }

        int nights = (int) DateUtil.getDiffInDays(checkinDate, checkoutDate);

        return repository.searchHomestay(
                request.getLongitude(),
                request.getLatitude(),
                request.getRadius(),
                checkinDate,
                checkoutDate,
                nights,
                request.getGuests(),
                request.getStatus().getValue());
    }
}
