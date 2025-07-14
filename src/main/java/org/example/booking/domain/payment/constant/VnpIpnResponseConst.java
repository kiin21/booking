package org.example.booking.domain.payment.constant;

import org.example.booking.domain.payment.dto.response.IpnResponse;

public class VnpIpnResponseConst {
    public static final IpnResponse SUCCESS = new IpnResponse("00", "Transaction successful");
    public static final IpnResponse SIGNATURE_INVALID = new IpnResponse("97", "Signature invalid");
    public static final IpnResponse ORDER_NOT_FOUND = new IpnResponse("01", "Order not found");
    public static final IpnResponse UNKNOWN_ERROR = new IpnResponse("99", "Unknown error occurred");
}
