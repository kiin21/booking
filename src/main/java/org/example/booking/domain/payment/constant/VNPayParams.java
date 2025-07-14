package org.example.booking.domain.payment.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VNPayParams {

    VERSION("vnp_Version"),
    COMMAND("vnp_Command"),
    TMN_CODE("vnp_TmnCode"),
    AMOUNT("vnp_Amount"),
    BANK_CODE("vnp_BankCode"),
    CREATE_DATE("vnp_CreateDate"),
    CURRENCY("vnp_CurrCode"),
    IP_ADDRESS("vnp_IpAddr"),
    LOCALE("vnp_Locale"),
    ORDER_INFO("vnp_OrderInfo"),
    ORDER_TYPE("vnp_OrderType"),
    RETURN_URL("vnp_ReturnUrl"),
    EXPIRE_DATE("vnp_ExpireDate"),
    TXN_REF("vnp_TxnRef"),
    SECURE_HASH("vnp_SecureHash"),
    SECURE_HASH_TYPE("vnp_SecureHashType");

    private final String value;
}
