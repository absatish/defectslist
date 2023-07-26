package com.sreeeservices.ui;

import com.sreeeservices.model.ButterflyResponse;
import com.sreeeservices.model.Company;

import java.util.List;
import java.util.stream.Collectors;

public class ButterflyTable {

    public static String getTabledContent(List<ButterflyResponse> butterflyResponses, final Company company) {
        String content = HeaderContent.getHeaderContent(company) +
                "\n" +
                "    <div class=\"table-container\">\n" +
                "        <table>\n" +
                "            <thead>\n" +
                "                <tr>\n" +
                "                    <th>Company</th>\n" +
                "                    <th>Call Id</th>\n" +
                "                    <th>Product</th>\n" +
                "                    <th>Name</th>\n" +
                "                    <th>Mobile</th>\n" +
                "                    <th>Address</th>\n" +
                "                    <th>Call Type</th>\n" +
                "                    <th>Action Taken On</th>\n" +
                "                    <th>Feedback</th>\n" +
                "                    <th>Status</th>\n" +
                "                    <th>Officially Allocated To</th>\n" +
                "                </tr>\n" +
                "            </thead>\n" +
                "            <tbody>\n" +
                getDynamicResponse(butterflyResponses) +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
        return content;
    }

    private static String getDynamicResponse(List<ButterflyResponse> butterflyResponses) {
        return butterflyResponses.stream()
                .map(response -> "                <tr>\n" +
                        "                    <td>Butterfly</td>\n" +
                        "                    <td>" + response.getCallId() + "</td>\n" +
                        "                    <td>" + response.getProduct() + "</td>\n" +
                        "                    <td>" + response.getCustomerName() + "</td>\n" +
                        "                    <td>" + response.getContactNumber() + "</td>\n" +
                        "                    <td>" + response.getAddress() + "</td>\n" +
                        "                    <td>" + response.getCallType() + "</td>\n" +
                        "                    <td></td>\n" +
                        "                    <td></td>\n" +
                        "                    <td>" + response.getStatus() + "</td>\n" +
                        "                    <td>" + response.getOfficiallyAllocatedTo() + "</td>\n" +
                        "                </tr>")
                .collect(Collectors.joining("\n"));
    }
}
