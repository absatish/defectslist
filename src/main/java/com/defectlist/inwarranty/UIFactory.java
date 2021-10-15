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
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<title>:: Generate Defectives List ::</title>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                "</script>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "<center><b><h4>Generate Defectives List</h4></b></center><hr>" +
                "\t\t\t\t<div class=\"form-group\">" +
                "<form name='billGenerator' method='GET' action='/app/v1/defects/login'>" +
                "<center>" +
                "<div class=\"container\" style=\"width:500px; align:center;\">\n" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Username\">Username</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-8\">  " +
                " <input class=form-control placeholder=username type=text name=username></div><br>" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Password\">Password</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-8\">  " +
                "<input class=form-control placeholder=password type=password name=password></div><br>" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"callId\"><img id=captcha2 src=" + captchaUrl + "> </label>\n" +
                "\t\t\t\t  <div class=\"col-sm-8\">  " +
                "<input class=form-control type=text name=captcha placeholder=captcha id=captcha></div><br>" +
                "<input type=hidden name=id value=" + jSessionId + ">" +
                "<input type=hidden name=server value=" + serverId + ">" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-9\" style=\"margin-top:0.5%;\" for=\"callId\"></label>\n" +
                "\t\t\t\t  <div class=\"col-sm-2\">  " +
                "<input class='btn btn-default' type=submit onclick=javascript:login()>" +
                "</table></center>" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }

    public String buildGridPage(final Map<String, List<GridItem>> gridItemsJson,
                                final URL verticalImage, final URL horizontalImage) {
        int colCount = 0;
        int rowCount = 0;
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
                if (rowCount != 3) {
                    webResponse = webResponse.concat("</tr><tr><td style='padding:0px;'><img src=" + horizontalImage + " width=300px></td><td></td><td><img src=" + horizontalImage + " width=300px></td><td></td><td><img src=" + horizontalImage + " width=300px></td></tr><tr>");
                } else {
                    webResponse = webResponse.concat("</tr></table><footer><br></footer><hr><br><table><tr>");
                    rowCount = -1;
                }
                colCount = 0;
                rowCount = rowCount + 1;
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
                "</style><style>" +
                "@media print {\n" +
                "  footer {page-break-after: always;}\n" +
                "}</style></head>\n<table><tr>";
    }

    private int getSortOrder(final GridItem item1, final GridItem item2) {
        return DefectivePartType.getPartTypeByName(item1.getSpareName()).getSortOrder()
                - DefectivePartType.getPartTypeByName(item2.getSpareName()).getSortOrder();
    }
}
