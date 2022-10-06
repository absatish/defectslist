package com.defectlist.inwarranty.ui;

import com.defectlist.inwarranty.model.DefectivePartType;
import com.defectlist.inwarranty.model.GridItem;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class UIFactory {

    public String getLoginPageV4(final String jSessionId, final String serverId, final URL captchaUrl) {
        return "<html>" +
                "   <head>" +
                "       <title>Defectives List Login Page</title>" +
                getHeaderV4() +
                        "<script type=text/javascript>" +
                        "function validate() {\n" +
                        "var userPrompt = true;\n" +
                        "if (document.getElementById('username').value=='') { \n" +
                        "       alert('Please enter username');\n" +
                        "       document.getElementById('username').focus();\n" +
                        "       userPrompt=false;\n" +
                        "} else if (document.getElementById('password').value=='') { \n" +
                        "       alert('Please enter password');\n" +
                        "       document.getElementById('password').focus();\n" +
                        "       userPrompt=false;\n" +
                        "} else if (document.getElementById('captcha').value=='') { \n" +
                        "       alert('Please enter captcha');\n" +
                        "       document.getElementById('captcha').focus();\n" +
                        "       userPrompt=false;\n" +
                        "} else if (document.getElementById('showOnlyNumbers').checked) { \n" +
                        "     userPrompt = confirm('Do you want to see only complaintIds? Are you sure?');\n" +
                        "} " +
                        "return userPrompt;       \n" +
                        "}\n" +
                        "var captcha = [];\n" +
                        "var captchaValue = '';\n" +
                        "function next(n) {\n" +
                        "    var length = document.getElementById('captcha' + (n-1)).value.length;\n" +
                        "    var key = event.keyCode || event.charCode;\n" +
                        "    if( key == 8 || key == 46 ){\n" +
                        "       if (length<=0 && n != 2) {\n" +
                        "          document.getElementById('captcha' + (n-2)).focus();" +
                        "       }\n" +
                        "    }\n" +
                        "   if (length == 1) {\n" +
                        "       captcha[n-2] = document.getElementById('captcha' + (n-1)).value;\n" +
                        "   }\n" +
                        "\n" +
                        "captchaValue = '';\n" +
                        "captcha.forEach(prepareCaptcha);\n" +
                        "   document.getElementById('captcha').value =  captchaValue;\n" +
                        "   if (n!=5 && length == 1) {\n" +
                        "       document.getElementById('captcha'+n).focus();\n" +
                        "   } else if (length > 1) {\n" +
                        "       document.getElementById('captcha'+(n-1)).value = '';\n" +
                        "   }\n" +
                        "}\n" +
                        "function prepareCaptcha(v) {\n" +
                        "   captchaValue += v + '';\n" +
                        "\n}       " +
                        "</script>" +
                "   </head>" +
                "   <body>" +
                "       <h2>Login Page</h2>\n" +
                "       <form name='billGenerator' id='billGenerator' method='POST' onsubmit=\"return validate();\" action='/app/v2/defects/login'>\n" +
                "       <div class=\"imgcontainer\">\n" +
                "       <img src=\"https://www.w3schools.com/howto/img_avatar2.png\" alt=\"Avatar\" class=\"avatar\">\n" +
                "       </div>\n" +
                "       <div class=\"container\">\n" +
                "       <label for=\"username\"><b>Username</b></label>\n" +
                "       <input id=\"username\" type=\"text\" placeholder=\"Enter Username\" name=\"username\" required>\n" +
                "       <label for=\"password\"><b>Password</b></label>\n" +
                "       <input id=\"password\" type=\"password\" placeholder=\"Enter Password\" name=\"password\" required>\n" +
                "       <label for=\"captcha\"><b>Captcha</b></label>\n" +
                "       <table width=100%><tr>" +
                "           <td><img src=\"" + captchaUrl + "\"></td>" +
                "           <td width=24%><input onkeyup=\"next(2)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha1\" id=\"captcha1\" required></td>" +
                "           <td width=24%><input onkeyup=\"next(3)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha2\" id=\"captcha2\" required></td>" +
                "           <td width=24%><input onkeyup=\"next(4)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha3\" id=\"captcha3\" required></td>" +
                "           <td width=24%><input onkeyup=\"next(5)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha4\" id=\"captcha4\" required></td>" +
                "       </tr></table>\n" +
                "       <label for=\"includeOther\"><b>Show 'Other' Complaints</b></label>\n" +
                "       <div class=\"gender\">" +
                "       <label>" +
                "       <input type=\"radio\" value=\"on\" name=\"includeOther\" required><span>Yes</span>\n" +
                "       </label>" +
                "       <label>" +
                "       <input type=\"radio\" value=\"off\" name=\"includeOther\" checked=\"checked\" required><span>No</span>\n" +
                "       </label>" +
                "       </div>" +
                "       <label for=\"showOnlyNumbers\"><b>Show Only Complaint Ids</b></label>\n" +
                "       <div class=\"gender\">" +
                "       <label>" +
                "       <input id=\"showOnlyNumbers\" type=\"radio\" value=\"on\" name=\"showOnlyNumbers\" required><span>Yes</span>\n" +
                "       </label>" +
                "       <label>" +
                "       <input type=\"radio\" value=\"off\" name=\"showOnlyNumbers\" checked=\"checked\" required><span>No</span>\n" +
                "       </label>" +
                "       </div>" +
                "       <input type=\"hidden\" name=\"id\" value=\"" + jSessionId + "\">\n" +
                "       <input type=\"hidden\" id=\"captcha\" name=\"captcha\">\n" +
                "       <input type=\"hidden\" name=\"server\" value=\"" + serverId + "\">\n" +
                "       <button type=\"submit\">Login</button>\n" +
                "       </div>\n" +
                "       </form>" +
                "   </body>" +
                "</html>";
    }

    public static String getFirstPageV4() {
        return "<html>" +
                "   <head>" +
                "       <title>Defectives List Login Page</title>" +
                getHeaderV4() +
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
                "   </head>" +
                "   <body>" +
                "       <h2>Login Page</h2>\n" +
                "       <form name='billGenerator' id='billGenerator' method='POST' action='/app/v2/defects/prelogin'>\n" +
                "       <div class=\"imgcontainer\">\n" +
                "       <img src=\"https://www.w3schools.com/howto/img_avatar2.png\" alt=\"Avatar\" class=\"avatar\">\n" +
                "       </div>\n" +
                "       <div class=\"container\">\n" +
                "       <label for=\"username\"><b>Username</b></label>\n" +
                "       <input type=\"text\" placeholder=\"Enter Username\" name=\"username\" required>\n" +
                "       <br>\n" +
                "       <button type=\"submit\">Proceed to Login</button>\n" +
                "       </div>\n" +
                "       </form>" +
                "   </body>" +
                "</html>";
    }

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
                "<form name='billGenerator' id='billGenerator' method='POST' action='/app/v2/defects/login'>" +
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
                "<div class=\"col-sm-4\">Include&nbsp;all&nbsp;complaints&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>" +
                "<label class=\"switch col-sm-6\">\n" +
                "  <input name=includeOther type=\"checkbox\">\n" +
                "  <span class=\"slider round\"></span>\n" +
                "</label><div class=\"form-group\"></div>" +
                "\t\t\t\t<hr><div class=\"form-group\">\n" +
                "<div class=\"col-sm-8\">&nbsp;&nbsp;Show&nbsp;only&nbsp;complaint&nbsp;numbers&nbsp;<font size=1px color=red> [ప్రతీ కంప్లైంట్  నెంబర్  మీద క్లిక్ చేసి డీటెయిల్స్ చూడవచ్చు]</font> </div>" +
                "<label class=\"switch col-sm-4\">\n" +
                "  <input name=showOnlyNumbers type=\"checkbox\">\n" +
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
        String webResponse = getGridItemHeader(gridItems.size());
        int totalCount = 0;
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
            final String branchName = gridItem.getTechName() != null && gridItem.getTechName().contains("VIKRAM") ? "ELURU" : "";
            webResponse = webResponse.concat("<td><table class='roundedCorners'  style='font-size:12px;'>" +
                    "<tr><td colspan=2><center>IN WARRANTY DEFECTIVE PARTS</center></td></tr>")
                    .concat("<tr><td style='width:80px;'>Branch Name</td><td id='bn" + totalCount + "'>" + branchName + "</td></tr>\n" +
                            "<tr><td>Complaint No</td><td>" + gridItem.getComplaintNumber() + "</td></tr>")
                    .concat("<tr><td>Date</td><td>" + gridItem.getDate() + "</td></tr>")
                    .concat("<tr><td>Product</td><td>" + gridItem.getProduct() + "</td></tr>")
                    .concat("<tr><td>Model Name</td><td>" + gridItem.getModel() + "</td></tr>")
                    .concat("<tr><td>Serial Number</td><td>" + gridItem.getSerialNumber() + "</td></tr>")
                    .concat("<tr><td>DOP</td><td>" + gridItem.getDop() + "</td></tr>")
                    .concat("<tr><td>Spare Name</td><td>" + gridItem.getSpareName() + "</td></tr>")
                    .concat("<tr><td>Actual Fault</td><td>" + gridItem.getActualFault() + "</td></tr>")
                    .concat("<tr><td>Tech Name</td><td id='tn" + totalCount + "'>" + gridItem.getTechName() + "</td></tr>")
                    .concat("</table></td>");
            colCount++;
            totalCount++;
        }
        webResponse = webResponse.concat("</tr></table>");
        webResponse = webResponse.concat("<button class=button onclick=\"window.print()\">Print this page</button>");

        return webResponse;
    }

    public String buildGridPageWithBillNumbers(final Map<String, List<GridItem>> gridItemsJson,
                                final URL verticalImage, final URL horizontalImage) {
        int colCount = 0;
        int rowCount = 0;
        final List<GridItem> gridItems = gridItemsJson.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .sorted(this::getSortOrder)
                .collect(Collectors.toList());
        String webResponse = getGridItemHeader(gridItems.size());
        int totalCount = 0;

        for(final GridItem gridItem : gridItems) {
            if (colCount > 0 && colCount < 3) {
            }
            if (colCount == 3) {
                if (rowCount != 3) {
                    webResponse = webResponse.concat("</tr><tr>");
                } else {
                    webResponse = webResponse.concat("</tr></table><footer><br></footer><hr><br><table><tr>");
                    rowCount = -1;
                }
                colCount = 0;
                rowCount = rowCount + 1;
            }
            final String id = gridItem.getComplaintNumber()+gridItem.getProduct().replaceAll(" ", "");
            Random random = new Random();
            random.nextInt(256*256*256);
            webResponse = webResponse.concat("<td style='cursor:pointer;color:" + String.format("#%06x", random.nextInt(256*256*256)) + ";' id='" + id + "'" +
                    " onclick=\"javascript:loadDetails('" + id + "');\">" + gridItem.getComplaintNumber() +
                    "&nbsp;-&nbsp;" + gridItem.getProduct() + "</td>");
            colCount++;
            totalCount++;
        }


        webResponse = webResponse.concat("</tr></table>");
        webResponse = webResponse.concat("<button class=button onclick=\"window.print()\">Print this page</button>");

        totalCount =0;
        for(final GridItem gridItem : gridItems) {
            final String id = gridItem.getComplaintNumber()+gridItem.getProduct().replaceAll(" ", "");
            final String branchName = gridItem.getTechName() != null && gridItem.getTechName().contains("VIKRAM") ? "ELURU" : "";

            webResponse = webResponse.concat("<p hidden onclick=\"javascript:close('" + gridItem.getComplaintNumber() +
                    "', '" + gridItem.getProduct() + "', '"+ id + "');\" id='hidden-" + id + "'><table class='roundedCorners'  style='font-size:12px;'>" +
                    "<tr><td colspan=2><center>IN WARRANTY DEFECTIVE PARTS</center></td></tr>" +
                    "<tr><td style='width:80px;'>Branch Name</td><td id='bn" + totalCount + "'>" + branchName + "</td></tr>" +
                    "<tr><td>Complaint No</td><td>" + gridItem.getComplaintNumber() + "</td></tr>" +
                    "<tr><td>Date</td><td>" + gridItem.getDate() + "</td></tr>" +
                    "<tr><td>Product</td><td>" + gridItem.getProduct() + "</td></tr>" +
                    "<tr><td>Model Name</td><td>" + gridItem.getModel() + "</td></tr>" +
                    "<tr><td>Serial Number</td><td>" + gridItem.getSerialNumber() + "</td></tr>" +
                    "<tr><td>DOP</td><td>" + gridItem.getDop() + "</td></tr>" +
                    "<tr><td>Spare Name</td><td>" + gridItem.getSpareName() + "</td></tr>" +
                    "<tr><td>Actual Fault</td><td>" + gridItem.getActualFault() + "</td></tr>" +
                    "<tr><td>Tech Name</td><td id='tn" + totalCount + "'>" + gridItem.getTechName() + "</td></tr></table></p>");
            totalCount++;
        }
        return webResponse;
    }

    public static String redirectToLatestVersion() {
        final String script =
                "function startTimer() {\n" +
                "    var presentTime1 = document.getElementById('timer').innerHTML;\n" +
                "    var presentTime = presentTime1.split(' ')[5];\n" +
                "    //var timeArray = presentTime.split(':');\n" +
                "    var m = 0;//timeArray[0];\n" +
                "    var s = checkSecond(presentTime - 1);\n" +
                "    if(s==59){m=m-1}\n" +
                "\n" +
                "    document.getElementById('timer').innerHTML =\n" +
                "     \"Redirecting to V2 Page in \" + s + \" Seconds\";\n" +
                "    console.log(m)\n" +
                "    // Check if the time is 0:00\n" +
                "    if (s == 0 && m == 0) { return };\n" +
                "    setTimeout(startTimer, 1000);\n" +
                "  }" +
                        "function checkSecond(sec) {\n" +
                        "    if (sec < 10 && sec >= 0) {sec = \"0\" + sec}; // add zero in front of numbers < 10\n" +
                        "    if (sec < 0) {sec = \"59\"};\n" +
                        "    return sec;\n" +
                        "  }";
        return "<html>" +
                "<title>Login Page</title>" +
                "<meta http-equiv = \"refresh\" content = \"3; url = /app/v2/defects\" />" +
                "\t\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                "<head><script type=text/javascript>" + script + "</script></head>" +
                "<body onload=javascript:startTimer()><br><br><br><br><br>" +
                "<center><font size=15px color=red><p id=timer>Redirecting to V2 Page in 4 Seconds</p> </font></center>";
    }

    private String getGridItemHeader(final int size) {
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
                "}</style>" +
                "<script>" +
                "function writeBranchName() {" +
                "for(var i=0;i<" + size + ";i++){" +
                "document.getElementById('bn'+i).innerHTML = document.getElementById('bn').value;" +
                "}" +
                "}" +
                "function writeTechnicianName() {" +
                "for(var i=0;i<" + size + ";i++){" +
                "document.getElementById('tn'+i).innerHTML = document.getElementById('tn').value;" +
                "}" +
                "}" +
                "function loadDetails(id) {" +
                "document.getElementById(id).innerHTML = document.getElementById('hidden-'+id).innerHTML;" +
                "}" +
                "function close(id, type, id2) {" +
                "document.getElementById(id).innerHTML = id2 + ' - ' + type;" +
                "}" +
                "function getcolor() {" +
                "Math.floor(Math.random()*16777215).toString(16);" +
                "}" +
                "</script>" +
                "</head>\n <button class=button onclick=\"window.print()\">Print this page</button>\n" +
                "Branch Name&nbsp;:&nbsp;<input name=branchName placeholder=branchName id=bn onchange='javascript:writeBranchName();'/>" +
                "&nbsp;&nbsp;Technician Name&nbsp;:&nbsp;<input name=technicianName placeholder=technicianName id=tn onchange='javascript:writeTechnicianName();'/>" +
                "<table><tr>";
    }

    private int getSortOrder(final GridItem item1, final GridItem item2) {
        return DefectivePartType.getPartTypeByName(item1.getSpareName()).getSortOrder()
                - DefectivePartType.getPartTypeByName(item2.getSpareName()).getSortOrder();
    }

    private String getSlider(final String name) {
        return  "</head>\n" +
                "<body>\n" +
                "<label class=\"switch\">\n" +
                "  <input type=\"checkbox\" name=\"" + name +"\">\n" +
                "  <span class=\"slider round\"></span>\n" +
                "</label>\n";
    }

    private String getStyles() {
        return "<style>\n" +
                ".switch {\n" +
                "  position: relative;\n" +
                "  display: inline-block;\n" +
                "  width: 50px;\n" +
                "  height: 22px;\n" +
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
                "  height: 20px;\n" +
                "  width: 20px;\n" +
                "  left: 4px;\n" +
                "  bottom: 1px;\n" +
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
                "  border-radius: 50px;\n" +
                "}\n" +
                "\n" +
                ".slider.round:before {\n" +
                "  border-radius: 50%;\n" +
                "}\n" +
                "</style>\n";
    }

    private static String getHeaderV4() {
        return "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <style>\n" +
                "body {font-family: Arial, Helvetica, sans-serif;}\n" +
                "form {border: 3px solid #f1f1f1;}\n" +
                "\n" +
                ".gender{\n" +
                "  font-family: Arial;\n" +
                "  display: flex;\n" +
                "  flex-wrap: wrap;\n" +
                "  width: 100%;\n" +
                "}\n" +
                ".gender label{\n" +
                "  position: relative;\n" +
                "  margin: 4px 15px 4px 0;\n" +
                "  display: inline-flex;\n" +
                "  justify-content: center;\n" +
                "  align-items: center;\n" +
                "  cursor: pointer;\n" +
                "}\n" +
                ".gender input{\n" +
                "  opacity: 0;\n" +
                "  position: absolute;\n" +
                "  left: 0;\n" +
                "  top: 0;\n" +
                "}\n" +
                ".gender label span{\n" +
                "  display: block;\n" +
                "  text-align: center;\n" +
                "  width: 100%;\n" +
                "  background: #fff;\n" +
                "  border: 1px solid #ccc;\n" +
                "  color: #bababa;\n" +
                "  font-size: 14px;\n" +
                "  border-radius: 4px;\n" +
                "  padding: 10px 20px;\n" +
                "  min-width: 70px;\n" +
                "  transition: 350ms;\n" +
                "}\n" +
                ".gender label:hover span, .gender label:hover span:before{\n" +
                "  border: 1px solid #777;\n" +
                "  color: #777;\n" +
                "}\n" +
                ".gender label span:before{\n" +
                "  content: '';\n" +
                "  position: absolute;\n" +
                "  width: 12px;\n" +
                "  height: 12px;\n" +
                "  border-radius: 50%;\n" +
                "  top: 50%;\n" +
                "  margin-top: -7px;\n" +
                "  left: 10px;\n" +
                "  border: 1px solid #ccc;\n" +
                "  transition: 500ms;\n" +
                "}\n" +
                ".gender input:checked ~ span{\n" +
                "  color: blue;\n" +
                "  border: 1px solid blue;\n" +
                "}\n" +
                ".gender label input:checked ~ span:before{\n" +
                "  background: blue;\n" +
                "  border: 1px solid blue;\n" +
                "}" +
                "input[type=text], input[type=password]{\n" +
                "  width: 100%;\n" +
                "  padding: 12px 20px;\n" +
                "  margin: 8px 0;\n" +
                "  display: inline-block;\n" +
                "  border: 1px solid #ccc;\n" +
                "  box-sizing: border-box;\n" +
                "}\n" +
                "input[type=number]{\n" +
                "  width: 100%;\n" +
                "  padding: 12px 10px;\n" +
                "  margin: 8px 0;\n" +
                "  display: inline-block;\n" +
                "  border: 1px solid #ccc;\n" +
                "  box-sizing: border-box;\n" +
                "}\n" +
                "\n" +
                "button {\n" +
                "  background-color: #04AA6D;\n" +
                "  color: white;\n" +
                "  padding: 14px 20px;\n" +
                "  margin: 8px 0;\n" +
                "  border: none;\n" +
                "  cursor: pointer;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "button:hover {\n" +
                "  opacity: 0.8;\n" +
                "}\n" +
                "\n" +
                ".cancelbtn {\n" +
                "  width: auto;\n" +
                "  padding: 10px 18px;\n" +
                "  background-color: #f44336;\n" +
                "}\n" +
                "\n" +
                ".imgcontainer {\n" +
                "  text-align: center;\n" +
                "  margin: 24px 0 12px 0;\n" +
                "}\n" +
                "\n" +
                "img.avatar {\n" +
                "  width: 15%;\n" +
                "  height: 10%;\n" +
                "  border-radius: 50%;\n" +
                "}\n" +
                "@media screen and (min-width: 768px) { " +
                "img.avatar {\n" +
                "  width: 15%;\n" +
                "  height: 20%;\n" +
                "  border-radius: 50%;\n" +
                "}\n" +
                "}" +
                "\n" +
                ".container {\n" +
                "  padding: 16px;\n" +
                "}\n" +
                "\n" +
                "span.psw {\n" +
                "  float: right;\n" +
                "  padding-top: 16px;\n" +
                "}\n" +
                "\n" +
                "/* Change styles for span and cancel button on extra small screens */\n" +
                "@media screen and (max-width: 300px) {\n" +
                "  span.psw {\n" +
                "     display: block;\n" +
                "     float: none;\n" +
                "  }\n" +
                "  .cancelbtn {\n" +
                "     width: 100%;\n" +
                "  }\n" +
                "}\n" +
                "</style>";
    }
}
