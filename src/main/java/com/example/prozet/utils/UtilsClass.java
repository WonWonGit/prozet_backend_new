package com.example.prozet.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilsClass {

    public static String getCurrentDate() {
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return current.format(formatter);
    }

    public static String date(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(dateTimeFormatter);
    }

    public static LocalDateTime stringToDate(String localDateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(localDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return dateTime;
    }

}
