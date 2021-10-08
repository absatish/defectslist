package com.defectlist.inwarranty;

import com.defectlist.inwarranty.model.DefectivePartType;
import com.defectlist.inwarranty.model.GridItem;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UIFactory {

    public String getLoginPage(final String jSessionId, final String serverId, final URL captchaUrl) {
        return "<form name='billGenerator' method='GET' action='/app/v1/defects/login'>" +
                "<table>" +
                "<tr><td> <input placeholder=username type=text name=username></td></tr>" +
                "<tr><td> <input placeholder=password type=password name=password></td></tr>" +
                "<tr><td>Captcha : <img id=captcha2 src=" + captchaUrl + "></td></tr>" +
                "<td><input type=text name=captcha placeholder=captcha id=captcha></td></tr>" +
                "<input type=hidden name=id value=" + jSessionId + ">" +
                "<input type=hidden name=server value=" + serverId + ">" +
                "<tr><td colspan=3 align=right><input type=submit onclick=javascript:login()></td></tr>" +
                "</table>" +
                "</form>";
    }

    public String buildGridPage(final Map<String, List<GridItem>> gridItemsJson,
                                final URL verticalImage, final URL horizontalImage) {
        int colCount = 0;
        final List<GridItem> gridItems = gridItemsJson.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .sorted(this::getSortOrder)
                .collect(Collectors.toList());
        String webResponse = getGridItemHeader();
        for(final GridItem gridItem : gridItems) {
            if (colCount > 0 && colCount < 3) {
                webResponse = webResponse.concat("<td><img src=" + verticalImage + " height=260px></td>");
            }
            if (colCount == 3) {
                webResponse = webResponse.concat("</tr><tr><td style='padding:0px;'><img src=" + horizontalImage + " width=300px></td><td></td><td><img src=" + horizontalImage + " width=300px></td><td></td><td><img src=" + horizontalImage + " width=300px></td></tr><tr>");
                colCount = 0;
            }
            webResponse = webResponse.concat("<td><table class='roundedCorners'  style='font-size:12px;'><tr><td colspan=2><center>IN WARRANTY DEFECTIVE PARTS</center></td></tr>")
                    .concat("<tr><td style='width:80px;'>Branch Name</td><td>ELURU</td></tr>\n<tr><td>Complaint No</td><td>" + gridItem.getComplaintNumber() + "</td></tr>")
                    .concat("<tr><td>Date</td><td>" + gridItem.getDate() + "</td></tr>")
                    .concat("<tr><td>Product</td><td>" + gridItem.getProduct() + "</td></tr>")
                    .concat("<tr><td>Model Name</td><td>" + gridItem.getModel() + "</td></tr>")
                    .concat("<tr><td>Serial Number</td><td>" + gridItem.getSerialNumber() + "</td></tr>")
                    .concat("<tr><td>DOP</td><td>" + gridItem.getDop() + "</td></tr>")
                    .concat("<tr><td>Spare Name</td><td>" + gridItem.getSpareName() + "</td></tr>")
                    .concat("<tr><td>Actual Fault</td><td>" + gridItem.getActualFault() + "</td></tr>")
                    .concat("<tr><td>Tech Name</td><td>" + gridItem.getTechName() + "</td></tr>")
                    .concat("</table></td>");
            colCount++;
        }
        webResponse = webResponse.concat("</tr></table>");
        return webResponse;
    }

    private String getGridItemHeader() {
        return "<html><title>Print</title><head>" +
                "<style type=text/css>td{font-size:12px;}\n.innertable{height:300px;width:300px;}</style>" +
                "<style> table { width:300px; height:250px; }" +
                "table.roundedCorners { border: 1.4px solid black; border-radius: 2px; border-spacing: 0; }" +
                "table.roundedCorners td," +
                "table.roundedCorners th { border-bottom: 1px solid black; }" +
                "table.roundedCorners tr:last-child > td { border-bottom: none; }" +
                "table.noStyles { border: none; border-radius: 13px; border-spacing: 0; }" +
                "table.noStyles td," +
                "table.roundedCorners th { border-bottom: none; padding: 2px; }" +
                "table.noStyles tr:last-child > td { border-bottom: none;padding: 2px; }" +
                "</style><style> @media print { .pageBreaks { page-break-before: always; } /* page-break-after works, as well */ }" +
                "</style></head>\n<table><tr>";
    }

    private int getSortOrder(final GridItem item1, final GridItem item2) {
        return DefectivePartType.getPartTypeByName(item1.getSpareName()).getSortOrder()
                - DefectivePartType.getPartTypeByName(item2.getSpareName()).getSortOrder();
    }
}
