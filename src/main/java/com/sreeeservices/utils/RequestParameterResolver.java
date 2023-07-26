package com.sreeeservices.utils;

import java.util.Map;
import java.util.Optional;

public class RequestParameterResolver {

    public static String getValue(final Map<String, String> requestParams, final String parameterKey) {
        return Optional.ofNullable(requestParams.get(parameterKey)).orElse("");
    }
}
