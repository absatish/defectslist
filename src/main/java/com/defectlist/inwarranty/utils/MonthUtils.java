package com.defectlist.inwarranty.utils;

import java.time.LocalDateTime;
import java.util.List;

public class MonthUtils {

    public static List<String> monthsList() {
        return List.of("January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December");
    }

    public static String currentMonth() {
        final String currentMonth = LocalDateTime.now().getMonth().name();
        return monthsList().stream()
                .filter(month -> month.equalsIgnoreCase(currentMonth))
                .findFirst().get();
    }
}
