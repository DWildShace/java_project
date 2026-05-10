package com.atm.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utils - hàm tiện ích định dạng tiền tệ và ngày giờ
 */
public class Utils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatCurrency(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    public static String formatDateTime(LocalDateTime dt) {
        if (dt == null) return "-";
        return dt.format(DATE_TIME_FORMATTER);
    }
}
