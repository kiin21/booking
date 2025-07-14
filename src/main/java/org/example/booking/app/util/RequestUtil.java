package org.example.booking.app.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();

            if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // If the IP address is the loopback address, return a more readable format
                ipAddress = "127.0.0.1";
            }
        }
        return ipAddress;
    }
}
