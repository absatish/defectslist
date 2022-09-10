package com.defectlist.inwarranty.utils;

import com.defectlist.inwarranty.model.FinancialDetails;

import java.util.*;
import java.util.stream.Collectors;

public class RequestParameterResolver {

    public static String getValue(final Map<String, String> requestParams, final String parameterKey) {
        return Optional.ofNullable(requestParams.get(parameterKey)).orElse("");
    }

    public static List<FinancialDetails> getFinanceRecords(final Map<String, String> requestParams) {
        Set<Integer> numbers = new HashSet<>();
        requestParams.keySet().stream()
                        .filter(key -> key.contains("amount"))
                        .map(key -> key.split("t")[1])
                        .map(Integer::valueOf)
                        .forEach(numbers::add);
        requestParams.keySet().stream()
                .filter(key -> key.contains("type"))
                .map(key -> key.split("e")[1])
                .map(Integer::valueOf)
                .forEach(numbers::add);
        requestParams.keySet().stream()
                .filter(key -> key.contains("description"))
                .map(key -> key.split("n")[1])
                .map(Integer::valueOf)
                .forEach(numbers::add);

        return numbers.stream()
                .map(number ->  {
                    final Integer id = requestParams.get("id" + number) == null
                            ? null
                            : Integer.valueOf(requestParams.get("id" + number));
                    return FinancialDetails.builder()
                            .id(id)
                            .amount(Integer.valueOf(requestParams.get("amount" + number)))
                            .description(requestParams.get("description" + number))
                            .financeType(requestParams.get("type" + number))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
