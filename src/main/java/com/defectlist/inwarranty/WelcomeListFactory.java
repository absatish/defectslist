package com.defectlist.inwarranty;

import java.util.Arrays;

public class WelcomeListFactory {

    private static final String USER_NAME = "User Name";

    static String getLoggedInUserName(final String welcomeListResponse) {
        try {
            return Arrays.stream(welcomeListResponse.split("\n"))
                    .filter(line -> line.contains(USER_NAME))
                    .map(line -> line.split("</td>")[1].split(">")[1])
                    .findFirst().orElse("");
        } catch (final Exception exception) {
            return "";
        }
    }
}
