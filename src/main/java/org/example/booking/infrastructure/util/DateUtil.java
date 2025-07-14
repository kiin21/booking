package org.example.booking.infrastructure.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class DateUtil {

    public static long getDiffInDays(LocalDate date1, LocalDate date2) {
        return ChronoUnit.DAYS.between(date1, date2);
    }

    public static LocalDate parse(String date) {
        return LocalDate.parse(date);
    }

    public static String formatVnTime(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date.getTime());
    }

//    public static void main(String[] args) {
//        // Example usage
//        LocalDate date1 = LocalDate.of(2023, 10, 1);
//        LocalDate date2 = LocalDate.of(2023, 10, 10);
//        long daysBetween = getDiffInDays(date1, date2);
//        System.out.println("Days between: " + daysBetween); // Output: Days between: 9
//
//        String formattedDate = formatVnTime(Calendar.getInstance());
//        System.out.println("Formatted VN Time: " + formattedDate);
//    }
}
