package com.atm.controller;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class DateTimeUtils {

    public static LocalDateTime getServerLocalTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now();
    }
}
