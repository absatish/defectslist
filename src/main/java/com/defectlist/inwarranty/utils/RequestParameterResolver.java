package com.defectlist.inwarranty.utils;

import java.util.*;
import java.util.stream.Collectors;

public class RequestParameterResolver {

    public static String getValue(final Map<String, String> requestParams, final String parameterKey) {
        return Optional.ofNullable(requestParams.get(parameterKey)).orElse("");
    }
}
