package org.example.booking.domain.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitPaymentRequest {

    private String userId;
    private Long amount;
    private String txnRef;
    private String ipAddress;
    private String requestId;
}
