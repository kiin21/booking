package org.example.booking.domain.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.domain.booking.service.BookingFactory;
import org.example.booking.domain.common.exception.BusinessException;
import org.example.booking.domain.payment.constant.VNPayParams;
import org.example.booking.domain.payment.constant.VnpIpnResponseConst;
import org.example.booking.domain.payment.dto.response.IpnResponse;
import org.example.booking.domain.payment.service.IpnHandler;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class VNPayIpnHandler implements IpnHandler {

    private final VNPayService vnPayService;

    private final BookingFactory bookingFactory;

    @Override
    public IpnResponse process(Map<String, String> params) {
        if (!vnPayService.verifyIpn(params)) {
            return VnpIpnResponseConst.SIGNATURE_INVALID;
        }

        IpnResponse response;

        var txnRef = params.get(VNPayParams.TXN_REF);
        try {
            var bookingId = String.valueOf(txnRef);
            var service = bookingFactory.build();

            service.markAsBooked(Long.valueOf(bookingId));

            response = VnpIpnResponseConst.SUCCESS;
        } catch (BusinessException e) {
            switch (e.getResponseCode()) {
                case BOOKING_NOT_FOUND -> response = VnpIpnResponseConst.ORDER_NOT_FOUND;
                default -> response = VnpIpnResponseConst.UNKNOWN_ERROR;
            }
        } catch (Exception e) {
            log.error("Error processing VNPay IPN for txnRef {}: {}", txnRef, e.getMessage(), e);
            response = VnpIpnResponseConst.UNKNOWN_ERROR;
        }

        log.info("VNPay IPN for txnRef {}: {}", txnRef, response);
        return response;
    }
}
