package org.example.booking.domain.payment.service;

import org.example.booking.domain.payment.dto.request.InitPaymentRequest;
import org.example.booking.domain.payment.dto.response.InitPaymentResponse;

public interface PaymentService {

    InitPaymentResponse initPayment(InitPaymentRequest request);
}
