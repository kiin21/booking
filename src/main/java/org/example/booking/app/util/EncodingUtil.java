package org.example.booking.app.util;

public class EncodingUtil {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];      // high nibble
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F]; // low nibble
        }

        return new String(hexChars);
    }

    private EncodingUtil() {
        // prevent instantiation
    }
}
