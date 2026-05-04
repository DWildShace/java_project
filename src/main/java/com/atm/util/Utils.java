package com.atm.util;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utils - hàm tiện ích định dạng tiền tệ và ngày giờ
 */
public class Utils {
    private static final Locale VIETNAM = new Locale("vi", "VN");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(VIETNAM);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatCurrency(double value) {
        return CURRENCY_FORMAT.format(value);
    }

    public static String formatDateTime(LocalDateTime dt) {
        if (dt == null) return "-";
        return dt.format(DATE_TIME_FORMATTER);
    }
}
