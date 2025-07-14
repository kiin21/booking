package org.example.booking.domain.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.app.constant.Locale;
import org.example.booking.domain.booking.constant.Currency;
import org.example.booking.domain.payment.constant.VNPayParams;
import org.example.booking.domain.payment.dto.request.InitPaymentRequest;
import org.example.booking.domain.payment.dto.response.InitPaymentResponse;
import org.example.booking.domain.payment.service.CryptoService;
import org.example.booking.domain.payment.service.PaymentService;
import org.example.booking.infrastructure.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayService implements PaymentService {

    private final RestClient.Builder builder;
    @Value("${payment.vnpay.timeout}")
    private int paymentTimeout;

    @Value("${payment.vnpay.init-payment-url}")
    private String initPaymentPrefixUrl;


    @Value("${payment.vnpay.return-url}")
    private String returnUrlFormat;

    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode;

    private final CryptoService cryptoService;

    private final int DEFAULT_MULTIPLIER = 100;
    private final String VERSION = "2.1.0";
    private final String COMMAND = "pay";
    private final String ORDER_TYPE = "170000";

    @SneakyThrows
    @Override
    public InitPaymentResponse initPayment(InitPaymentRequest request) {
        var amount = request.getAmount() * DEFAULT_MULTIPLIER;
        var txnRef = request.getTxnRef();
        var returnUrl = buildReturnUrl(txnRef);
        var vnCalendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        var createdDate = DateUtil.formatVnTime(vnCalendar);
        vnCalendar.add(Calendar.MINUTE, paymentTimeout);
        var expiredData = DateUtil.formatVnTime(vnCalendar);

        var ipAddress = request.getIpAddress();
        var orderInfo = buildPaymentDetail(request);
        var requestId = request.getRequestId();

        Map<String, String> params = new HashMap<>();

        params.put(VNPayParams.VERSION.getValue(), VERSION);
        params.put(VNPayParams.COMMAND.getValue(), COMMAND);
        params.put(VNPayParams.TMN_CODE.getValue(), tmnCode);
        params.put(VNPayParams.AMOUNT.getValue(), String.valueOf(amount));
        params.put(VNPayParams.CURRENCY.getValue(), Currency.VND.getValue());

        params.put(VNPayParams.TXN_REF.getValue(), txnRef);
        params.put(VNPayParams.RETURN_URL.getValue(), returnUrl);

        params.put(VNPayParams.CREATE_DATE.getValue(), createdDate);
        params.put(VNPayParams.EXPIRE_DATE.getValue(), expiredData);

        params.put(VNPayParams.IP_ADDRESS.getValue(), ipAddress);
        params.put(VNPayParams.LOCALE.getValue(), Locale.VIETNAME.getValue());

        params.put(VNPayParams.ORDER_INFO.getValue(), orderInfo);
        params.put(VNPayParams.ORDER_TYPE.getValue(), ORDER_TYPE);

        var initPaymentUrl = buildInitPaymentUrl(params);
        log.debug("[request_id={}] VNPay init payment URL: {}", requestId, initPaymentUrl);
        return InitPaymentResponse.builder()
                .vnpUrl(initPaymentUrl)
                .build();

    }

    private String buildPaymentDetail(InitPaymentRequest request) {
        return String.format("Checkout booking request %s for user %s",
                request.getTxnRef(), request.getUserId());
    }

    private String buildReturnUrl(String txnRef) {
        return String.format(returnUrlFormat, txnRef);
    }

    public boolean verifyIpn(Map<String, String> params) {
        var reqSecureHash = params.get(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH_TYPE);
        var hashPayload = new StringBuilder();
        var fieldnames = new ArrayList<>(params.keySet());
        Collections.sort(fieldnames);

        var itr = fieldnames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashPayload.append(fieldName);
                hashPayload.append("=");
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    hashPayload.append("&");
                }
            }
        }

        var secureHash = cryptoService.sign(hashPayload.toString());
        return secureHash.equals(reqSecureHash);
    }

    private String buildInitPaymentUrl(Map<String, String> params) throws UnsupportedEncodingException {
        var hashPayload = new StringBuilder();
        var query = new StringBuilder();

        var fieldnames = new ArrayList<>(params.keySet());
        Collections.sort(fieldnames);

        var itr = fieldnames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashPayload.append(fieldName);
                hashPayload.append("=");
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    query.append("&");
                    hashPayload.append("&");
                }
            }
        }

        var secureHash = cryptoService.sign(hashPayload.toString());

        query.append("&vnp_SecureHash=");
        query.append(secureHash);

        return initPaymentPrefixUrl + "?" + query;
    }
}
