package com.sreeeservices.mapper;

import com.sreeeservices.model.ButterflyResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlToDtoMapper {

    private static final String REGEX = "<td class='altrow' align='left' >";

    public static List<ButterflyResponse> toButterflyResponse(final String butterflyHtmlResponse) {
        List<String> responseLines = Arrays.asList(butterflyHtmlResponse.split("\n"));
        List<ButterflyResponse> butterflyResponses = new ArrayList<>();
        ButterflyResponse butterflyResponse = new ButterflyResponse();
        for (String line: responseLines) {
            if (line.contains(REGEX)) {
                String content = line.split(">")[1].split("<")[0];
                if (line.contains("CALL_ID")) {
                    butterflyResponse.setCallId(content);
                }
                if (line.contains("PRODUCT")) {
                    butterflyResponse.setProduct(content);
                }
                if (line.contains("Name")) {
                    butterflyResponse.setCustomerName(content);
                }
                if (line.contains("Mobile")) {
                    butterflyResponse.setContactNumber(content);
                }
                if (line.contains("Address")) {
                    butterflyResponse.setAddress(content);
                }
                if (line.contains("Brand")) {
                    butterflyResponse.setBrand(content);
                }
                if (line.contains("Status")) {
                    butterflyResponse.setStatus(content);
                }
                if (line.contains("Call Type")) {
                    butterflyResponse.setCallType(content);
                }
                if (line.contains("Engineer")) {
                    butterflyResponse.setOfficiallyAllocatedTo(content);
                    butterflyResponses.add(butterflyResponse);
                    butterflyResponse = new ButterflyResponse();
                }
            }
        }
        return butterflyResponses;
    }
}
