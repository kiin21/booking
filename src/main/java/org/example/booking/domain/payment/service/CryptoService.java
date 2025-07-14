package org.example.booking.domain.payment.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.app.util.EncodingUtil;
import org.example.booking.domain.common.constant.ResponseCode;
import org.example.booking.domain.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class CryptoService {

    @Value("${payment.vnpay.secret-key}")
    private String secretKey;

    private Mac mac;

    @PostConstruct
    public void init() {
        try {
            mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKeySpec);
        } catch (Exception e) {
            log.error("Failed to initialize HMAC for VNPAY", e);
            throw new BusinessException(ResponseCode.VNPAY_SIGNING_FAILED);
        }
    }

    public String sign(String payload) {
        try {
            return EncodingUtil.toHexString(mac.doFinal(payload.getBytes()));
        } catch (Exception e) {
            log.error("Error signing payload: {}", payload, e);
            throw new BusinessException(ResponseCode.VNPAY_SIGNING_FAILED);
        }
    }
}
