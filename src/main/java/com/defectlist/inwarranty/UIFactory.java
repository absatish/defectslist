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
                "<style>" +
                ".switch {\n" +
                "  position: relative;\n" +
                "  display: inline-block;\n" +
                "  width: 52px;\n" +
                "  height: 24px;\n" +
                "}\n" +
                "\n" +
                ".switch input { \n" +
                "  opacity: 0;\n" +
                "  width: 0;\n" +
                "  height: 0;\n" +
                "}\n" +
                "\n" +
                ".slider {\n" +
                "  position: absolute;\n" +
                "  cursor: pointer;\n" +
                "  top: 0;\n" +
                "  left: 0;\n" +
                "  right: 0;\n" +
                "  bottom: 0;\n" +
                "  background-color: #ccc;\n" +
                "  -webkit-transition: .4s;\n" +
                "  transition: .4s;\n" +
                "}\n" +
                "\n" +
                ".slider:before {\n" +
                "  position: absolute;\n" +
                "  content: \"\";\n" +
                "  height: 18px;\n" +
                "  width: 18px;\n" +
                "  left: 4px;\n" +
                "  bottom: 3px;\n" +
                "  background-color: white;\n" +
                "  -webkit-transition: .4s;\n" +
                "  transition: .4s;\n" +
                "}\n" +
                "\n" +
                "input:checked + .slider {\n" +
                "  background-color: #2196F3;\n" +
                "}\n" +
                "\n" +
                "input:focus + .slider {\n" +
                "  box-shadow: 0 0 1px #2196F3;\n" +
                "}\n" +
                "\n" +
                "input:checked + .slider:before {\n" +
                "  -webkit-transform: translateX(26px);\n" +
                "  -ms-transform: translateX(26px);\n" +
                "  transform: translateX(26px);\n" +
                "}\n" +
                "\n" +
                "/* Rounded sliders */\n" +
                ".slider.round {\n" +
                "  border-radius: 35px;\n" +
                "}\n" +
                "\n" +
                ".slider.round:before {\n" +
                "  border-radius: 50%;\n" +
                "}</style>" +
                "<script type=text/javascript>" +
                "function validate() {" +
                "if (document.getElementById('username').value==''){" +
                "alert('Please enter username');" +
                "document.getElementById('username').focus();" +
                "} else if (document.getElementById('password').value==''){" +
                "alert('Please enter password');" +
                "document.getElementById('password').focus();" +
                "} else if (document.getElementById('captcha').value==''){" +
                "alert('Please enter captcha');" +
                "document.getElementById('captcha').focus();" +
                "} else {" +
                "document.getElementById('billGenerator').submit();" +
                "}}" +
                "</script>" +
                "\t</head>\n" +
                "\t<body>\n" +
                "<center><b><h4>Generate Defectives List</h4></b></center><hr>" +
                "\t\t\t\t<div class=\"form-group\">" +
                "<form name='billGenerator' id='billGenerator' method='POST' action='/app/v1/defects/login'>" +
                "<center>" +
                "<div class=\"container\" style=\"width:500px; align:center;\">\n" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Username\">Username</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-6\">  " +
                " <input class=form-control placeholder=username type=text id=username name=username></div>" +
                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-username></div>" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Password\">Password</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-6\">  " +
                "<input class=form-control placeholder=password type=password id=password name=password></div>" +
                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-password></div>" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"callId\"><img id=captcha2 src=" + captchaUrl + "> </label>\n" +
                "\t\t\t\t  <div class=\"col-sm-6\">  " +
                "<input class=form-control type=text name=captcha placeholder=captcha id=captcha></div>" +
                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-captcha></div>" +

                "<input type=hidden name=id id=sessionId value=" + jSessionId + ">" +
                "<input type=hidden name=server id=serverId value=" + serverId + ">" +

                "\t\t\t\t<hr><div class=\"form-group\">\n" +
                "<div class=\"col-sm-4\">Include&nbsp;all&nbsp;complaints</div>" +
                "<label class=\"switch col-sm-6\">\n" +
                "  <input name=includeOther type=\"checkbox\">\n" +
                "  <span class=\"slider round\"></span>\n" +
                "</label>" +
                "\t\t\t\t<hr><div class=\"form-group\">\n" +
                "\t\t\t\t  <div class=\"col-sm-12\">  " +
                "<input class='btn btn-default' style='align:right;' name='Login' " +
                "value='Login' type=button onclick=javascript:validate()></div></div>" +
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

            webResponse = webResponse.concat("<td><table class='roundedCorners'  style='font-size:12px;'>" +
                    "<tr><td colspan=2><center>IN WARRANTY DEFECTIVE PARTS</center></td></tr>")
                    .concat("<tr><td style='width:80px;'>Branch Name</td><td>ELURU</td></tr>\n" +
                            "<tr><td>Complaint No</td><td>" + gridItem.getComplaintNumber() + "</td></tr>")
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
        webResponse = webResponse.concat("<button class=button onclick=\"window.print()\">Print this page</button>");

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
                "}</style></head>\n <button class=button onclick=\"window.print()\">Print this page</button>\n<table><tr>";
    }

    private int getSortOrder(final GridItem item1, final GridItem item2) {
        return DefectivePartType.getPartTypeByName(item1.getSpareName()).getSortOrder()
                - DefectivePartType.getPartTypeByName(item2.getSpareName()).getSortOrder();
    }
}
