package com.sreeeservices.ui;

import com.sreeeservices.model.Company;
import com.sreeeservices.model.GlenResponse;

import java.util.List;
import java.util.stream.Collectors;

public class GlenTable {

    public static String getTabledContent(List<GlenResponse> glenResponses) {
        String content = HeaderContent.getHeaderContent(Company.GLEN) +
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
                "                    <th>City</th>\n" +
                "                    <th>Action Taken On</th>\n" +
                "                    <th>Feedback</th>\n" +
                "                    <th>Status</th>\n" +
                "                </tr>\n" +
                "            </thead>\n" +
                "            <tbody>\n" +
                getDynamicResponse(glenResponses) +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
        return content;
    }

    private static String getDynamicResponse(List<GlenResponse> glenResponses) {
        return glenResponses.stream()
                .map(glenResponse -> "                <tr>\n" +
                        "                    <td>Glen</td>\n" +
                        "                    <td>" + glenResponse.getServiceBookingNumber() + "</td>\n" +
                        "                    <td>" + "Product" + "</td>\n" +
                        "                    <td>" + glenResponse.getCustomerName() + "</td>\n" +
                        "                    <td>" + glenResponse.getCustomerMobileNumber() + "</td>\n" +
                        "                    <td>" + glenResponse.getCity() + "," + glenResponse.getState() + " - " + glenResponse.getPin() +"</td>\n" +
                        "                    <td></td>\n" +
                        "                    <td>" + glenResponse.getCity() + "</td>\n" +
                        "                    <td></td>\n" +
                        "                    <td></td>\n" +
                        "                    <td>" + glenResponse.getStatus() + "</td>\n" +
                        "                </tr>")
                .collect(Collectors.joining("\n"));
    }
}
