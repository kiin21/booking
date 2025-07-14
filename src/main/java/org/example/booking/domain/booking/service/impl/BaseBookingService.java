package org.example.booking.domain.booking.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.domain.booking.constant.AvailabilityStatus;
import org.example.booking.domain.booking.constant.BookingStatus;
import org.example.booking.domain.booking.dto.BookingPrice;
import org.example.booking.domain.booking.dto.request.BookingRequest;
import org.example.booking.domain.booking.dto.response.BookingResponse;
import org.example.booking.domain.booking.dto.response.BookingStatusResponse;
import org.example.booking.domain.booking.entity.Booking;
import org.example.booking.domain.booking.entity.HomestayAvailability;
import org.example.booking.domain.booking.mapper.BookingMapper;
import org.example.booking.domain.booking.repository.BookingRepository;
import org.example.booking.domain.booking.repository.HomestayAvailabilityRepository;
import org.example.booking.domain.booking.service.AvailabilityService;
import org.example.booking.domain.booking.service.BookingService;
import org.example.booking.domain.booking.service.PricingService;
import org.example.booking.domain.common.constant.ResponseCode;
import org.example.booking.domain.common.exception.BusinessException;
import org.example.booking.domain.homestay.constant.HomestayStatus;
import org.example.booking.domain.homestay.entity.Homestay;
import org.example.booking.domain.homestay.service.HomestayService;
import org.example.booking.domain.payment.dto.request.InitPaymentRequest;
import org.example.booking.domain.payment.service.PaymentService;
import org.example.booking.infrastructure.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseBookingService implements BookingService {

    private static final int NIGHT_MAX = 365;

    private final BookingRepository bookingRepository;
    private final HomestayAvailabilityRepository availabilityRepository;
    private final AvailabilityService availabilityService;
    private final HomestayService homestayService;
    private final PricingService pricingService;
    private final BookingMapper mapper;
    private final PaymentService paymentService;

    @Override
    @SneakyThrows
    @Transactional
    public BookingResponse book(BookingRequest request) {
        // Validate the request: checking for date and guest constraints
        validateRequest(request);

        // Validate the homestay: checking if it exists, is active, and has enough capacity
        Homestay homestay = validateHomestay(request);

        // Check availability: locking the homestay availability for the requested dates
        var aDays = checkAvailability(homestay, request);

        var price = pricingService.calculate(homestay, aDays);
        var booking = buildBooking(request, price);

        booking.setStatus(BookingStatus.PROCESSING.getValue());
        aDays.forEach(a -> a.setStatus(AvailabilityStatus.HELD.getValue()));

        availabilityRepository.saveAll(aDays);
        bookingRepository.save(booking);

        var initPaymentRequest = InitPaymentRequest.builder()
                .userId(request.getUserId())
                .amount(booking.getTotalAmount().longValue())
                .txnRef(String.valueOf(booking.getId()))
                .requestId(booking.getRequestId())
                .ipAddress(request.getIpAddress())
                .build();

        var initPaymentResponse = paymentService.initPayment(initPaymentRequest);

        sendNotifications(booking);
        postProcess(booking);   // hook method

        log.info("[request_id={}] User user_id={} created booking_id={} successfully", request.getRequestId(), request.getUserId(), booking.getId());
        BookingResponse.Booking bookingDto = mapper.toBookingDTO(booking);

        return BookingResponse.builder()
                .booking(bookingDto)
                .payment(initPaymentResponse)
                .build();
    }

    @Transactional
    @Override
    public void markAsBooked(Long bookingId) {
        final var bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new BusinessException(ResponseCode.BOOKING_NOT_FOUND);
        }

        final var booking = bookingOpt.get();
        final var availableDays = availabilityService.checkAvailabilityForBooking(
                booking.getHomestayId(),
                booking.getCheckinDate(),
                booking.getCheckoutDate()
        );

        booking.setStatus(BookingStatus.BOOKED.getValue());
        availableDays.forEach(a -> a.setStatus(AvailabilityStatus.BOOKED.getValue()));

        availabilityService.saveAll(availableDays);
        bookingRepository.save(booking);

        mapper.toBookingDTO(booking);
    }


    public BookingStatusResponse getBookingStatus(Long bookingId) {
        final var bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new BusinessException(ResponseCode.BOOKING_NOT_FOUND);
        }

        return mapper.toBookingStatusResponse(bookingOpt.get());
    }

    protected void validateRequest(final BookingRequest request) {
        final var checkinDate = request.getCheckinDate();
        final var checkoutDate = request.getCheckoutDate();
        final var currentDate = LocalDate.now();

        if (checkinDate.isBefore(currentDate) || checkinDate.isAfter(checkoutDate)) {
            throw new BusinessException(ResponseCode.CHECKIN_DATE_INVALID);
        }

        if (request.getGuests() <= 0) {
            throw new BusinessException(ResponseCode.GUESTS_INVALID);
        }
    }

    protected Homestay validateHomestay(final BookingRequest request) {
        final var homestay = homestayService.getHomestayById(Long.valueOf(request.getHomestayId()));
        if (homestay == null) {
            throw new BusinessException(ResponseCode.HOMESTAY_NOT_FOUND);
        }

        if (homestay.getStatus() != HomestayStatus.ACTIVE.getValue()) {
            throw new BusinessException(ResponseCode.HOMESTAY_NOT_ACTIVE);
        }

        if (request.getGuests() > homestay.getGuests() || request.getGuests() <= 0) {
            throw new BusinessException(ResponseCode.GUESTS_INVALID);
        }

        return homestay;
    }


    protected List<HomestayAvailability> checkAvailability(Homestay homestay, BookingRequest request) {
        final Long homestayId = homestay.getId();
        final LocalDate checkinDate = request.getCheckinDate();
        final LocalDate checkoutDate = request.getCheckoutDate();
        final int nights = (int) DateUtil.getDiffInDays(checkinDate, checkoutDate);
        if (nights > NIGHT_MAX) {
            throw new BusinessException(ResponseCode.NIGHTS_INVALID);
        }

        log.debug("[request_id={}] User user_id={} is acquiring lock homestay_id={} from checkin_date={} to checkout_date={}", request.getRequestId(), request.getUserId(), homestayId, checkinDate, checkoutDate);
        final var aDays = availabilityRepository.findAndLockHomestayAvailability(
                homestayId,
                AvailabilityStatus.AVAILABLE.getValue(),
                checkinDate,
                checkoutDate
        );

        log.debug("[request_id={}] User user_id={} locked homestay_id={} from checkin_date={} to checkout_date={}", request.getRequestId(), request.getUserId(), homestayId, checkinDate, checkoutDate);
        // If there is sufficient available days, the size of aDays should be equal to nights + 1
        if (aDays.isEmpty() || aDays.size() != nights + 1) {
            throw new BusinessException(ResponseCode.HOMESTAY_BUSY);
        }

        return aDays;
    }

    protected Booking buildBooking(BookingRequest request, BookingPrice price) {
        final LocalDate checkinDate = request.getCheckinDate();
        final LocalDate checkoutDate = request.getCheckoutDate();

        return Booking.builder()
                .homestayId(Long.valueOf(request.getHomestayId()))
                .userId(Long.valueOf(request.getUserId()))
                .checkinDate(checkinDate)
                .checkoutDate(checkoutDate)
                .guests(request.getGuests())
                .subtotal(price.getSubtotal())
                .discount(price.getDiscount())
                .totalAmount(price.getTotalAmount())
                .currency(price.getCurrency())
                .note(request.getNote())
                .status(BookingStatus.BOOKED.getValue())
                .requestId(request.getRequestId())
                .build();
    }

    protected void sendNotifications(Booking booking) {
        log.info("Sending email to user={}", booking.getUserId());
    }

    protected void postProcess(Booking booking) {
    }
}