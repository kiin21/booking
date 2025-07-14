package org.example.booking.domain.payment.service;

import org.example.booking.domain.payment.dto.response.IpnResponse;

import java.util.Map;

public interface IpnHandler {
    IpnResponse process(Map<String, String> params);
}
