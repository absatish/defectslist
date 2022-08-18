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

    public static String getFirstPageV3() {
        return "<html>" +
                "<title>Login Page</title>" +
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
                "} else {" +
                "document.getElementById('billGenerator').submit();" +
                "}}" +
                "</script>" +
                "\t</head>\n" +
                "<center><b><h4>Generate Defectives List</h4></b></center><hr>" +
                "\t\t\t\t<div class=\"form-group\">" +
                "Please enter userId to proceed further.<br><hr>" +
                "<form name='billGenerator' id='billGenerator' method='POST' action='/app/v2/defects/prelogin'>" +
                "<center>" +
                "<div class=\"container\" style=\"width:500px; align:center;\">\n" +
               " <input class=form-control placeholder=username type=text id=username name=username></div>" +
               "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-username></div>" +
               "\t\t\t\t<hr><div class=\"form-group\">\n" +
               "\t\t\t\t  <div class=\"col-sm-12\">  " +
               "<input class='btn btn-default' style='align:right;' name='Login' " +
               "value='Login' type=button onclick=javascript:validate()></div></div>" +
               "</table></center>" +
                "" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }
    
    public static String getFirstPage() {
        return "<html>" +
                "<title>Login Page</title>" +
                "<meta http-equiv = \"refresh\" content = \"5; url = https://absatish.github.io/defectslist-ui\" />" +
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
                "} else {" +
                "document.getElementById('billGenerator').submit();" +
                "}}" +
                "</script>" +
                "\t</head>\n" +
                "<center><b><h4>Generate Defectives List</h4></b></center><hr>" +
                "\t\t\t\t<div class=\"form-group\">" +
                "<form name='billGenerator' id='billGenerator' method='POST' action='/app/v2/defects/prelogin'>" +
                "<center>" +
                "<div class=\"container\" style=\"width:500px; align:center;\">\n" +
//                " <input class=form-control placeholder=username type=text id=username name=username></div>" +
//                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-username></div>" +
//                "\t\t\t\t<hr><div class=\"form-group\">\n" +
//                "\t\t\t\t  <div class=\"col-sm-12\">  " +
//                "<input class='btn btn-default' style='align:right;' name='Login' " +
//                "value='Login' type=button onclick=javascript:validate()></div></div>" +
//                "</table></center>" +
                "" +
                "Please use our front end services." +
                "<br> Please wait while we redirect you to <a href=https://absatish.github.io/defectslist-ui>https://absatish.github.io/defectslist-ui " +
                "</form>\n" +
                "</body>\n" +
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

    public String getLoginPageV2(final String jSessionId, final String serverId, final URL captchaUrl) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <script src=\"https://kit.fontawesome.com/2a4e6a3153.js\" crossorigin=\"anonymous\"></script>\n" +
                "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style> " +
                "           body {\n" +
                "\tbackground: linear-gradient(90deg, #fc8b6a, #e10467);\t\t\n" +
                "}\n" +
                "\n" +
                "* {\n" +
                "\tbox-sizing: border-box;\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\t\n" +
                "\tfont-family: Raleway, sans-serif;\n" +
                "}\n" +
                "\n" +
                ".card {\t\t\n" +
                "\tbackground: linear-gradient(90deg, white , white);\t\t\n" +
                "\tposition: relative;\t\n" +
                "\theight: 550px;\n" +
                "\twidth: 400px;\t\n" +
                "\tbox-shadow: 0px 0px 24px #5C5696;\n" +
                "    opacity: 85%;\n" +
                "    align-content: flex-end;\n" +
                "    margin: auto;\n" +
                "    margin-top: 8%;\n" +
                "    padding-top: 2%;\n" +
                "    padding-bottom: 2%;\n" +
                "}\n" +
                "\n" +
                ".h1{\n" +
                "    text-align: center;\n" +
                "    padding-bottom: 2%;\n" +
                "}\n" +
                ".icon{\n" +
                "    color: red;\n" +
                "    background-color: #fa2e00;\n" +
                "}\n" +
                "\n" +
                ".line{\n" +
                "        height: 1px;\n" +
                "        background: rgb(255, 0, 0);\n" +
                "}\n" +
                ".container{\n" +
                "    margin-top: 10%;\n" +
                "    padding-left: 10%;\n" +
                "}\n" +
                ".label{\n" +
                "    font-size: large;\n" +
                "    font-family: 'Poppins', sans-serif;\n" +
                "    display: inline-block;\n" +
                "    margin-top: 3%;\n" +
                "    margin-bottom: 3%;\n" +
                "}\n" +
                ".box{\n" +
                "    color: #000000;\n" +
                "    border: none;\n" +
                "    background-color: transparent;\n" +
                "    resize: none;\n" +
                "    outline: none;\n" +
                "    height: 15px;\n" +
                "    background: rgba(255, 0, 0, 0);\n" +
                "    font-family: 'Poppins', sans-serif;\n" +
                "    border-bottom : 2.2px solid #fc8b6a;\n" +
                "    opacity: 75%;\n" +
                "    padding-right: 5%;\n" +
                "\n" +
                "    \n" +
                "}\n" +
                ".box:hover{\n" +
                "    color: red;\n" +
                "    border-bottom : 2.6px solid rgb(255, 1, 1);\n" +
                "}\n" +
                ".submit{\n" +
                "    margin-top: 10%;  \n" +
                "    text-align: center;\n" +
                "    border: 1px solid rgb(254, 5, 5);\n" +
                "    color: #fc8b6a;\n" +
                "    background: transparent;\n" +
                "    font-size: 14px;\n" +
                "    padding: 16px 16px;\n" +
                "    border-radius: 26px;\n" +
                "    border: 1px solid #d4d3e800;\n" +
                "    text-transform: uppercase;\n" +
                "    font-weight: 700;\n" +
                "    display: flex;\n" +
                "    align-items: center;\n" +
                "    width: 34%;\n" +
                "    color: #fa2e00;\n" +
                "    box-shadow: 0px 2px 2px #ffcfcf;\n" +
                "    cursor: pointer;\n" +
                "    transition: .2s;\n" +
                "    align-items: center;\n" +
                "    margin-top: 35px;\n" +
                "    justify-content: center;\n" +
                "\n" +
                "    }\n" +
                ".check{\n" +
                "    margin-top: 3%;\n" +
                "}\n" +
                ".check1{\n" +
                "    margin-top: 18%;\n" +
                "    \n" +
                "}\n" +
                ".submit:hover{\n" +
                "    color: white;\n" +
                "    background-color: #fa2e00;\n" +
                "}\n" +
                ".check:checked::before{\n" +
                "    color: red;\n" +
                "\n" +
                "    background-color: #fa2e00;\n" +
                "}\n" +
                ".check:checked::after{\n" +
                "    background-color:green;\n" +
                "    color: red;\n" +
                "    }\n" +
                "\n" +
                ".social{\n" +
                "    display: flex;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "@media  (max-width: 600px){\n" +
                "    body {\n" +
                "        box-sizing: border-box;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\t\n" +
                "        font-family: Raleway, sans-serif;\n" +
                "    }\n" +
                "    .card {\t\t\n" +
                "        background: linear-gradient(90deg, white , white);\t\t\n" +
                "        position: relative;\t\n" +
                "        height: 450px;\n" +
                "        width: 280px;\t\n" +
                "        box-shadow: 0px 0px 24px #5C5696;\n" +
                "        opacity: 85%;\n" +
                "        align-content: flex-end;\n" +
                "        padding-top: 2%;\n" +
                "        padding-bottom: 0%;\n" +
                "        margin-top: 22%;\n" +
                "        margin-bottom: -13cm;\n" +
                "\n" +
                "\n" +
                "    }\n" +
                "    .box{\n" +
                "        color: #000000;\n" +
                "        border: none;\n" +
                "        background-color: transparent;\n" +
                "        resize: none;\n" +
                "        outline: none;\n" +
                "        height: 15px;\n" +
                "        background: rgba(255, 0, 0, 0);\n" +
                "        font-family: 'Poppins', sans-serif;\n" +
                "        border-bottom : 2.2px solid #fc8b6a;\n" +
                "        opacity: 75%;\n" +
                "        padding-right: 5%;\n" +
                "    \n" +
                "        \n" +
                "    }\n" +
                "    .box:hover{\n" +
                "        color: red;\n" +
                "        border-bottom : 2.6px solid rgb(255, 1, 1);\n" +
                "    }\n" +
                "}" +
                "   </style>" +
                "    <title>login</title>\n" +
                "    <script type=text/javascript>\n" +
                "        function validate() {\n" +
                "            if (document.getElementById('username').value=='') {\n" +
                "                alert('Please enter username');\n" +
                "                document.getElementById('username').focus();\n" +
                "            } else if (document.getElementById('password').value=='') {\n" +
                "                alert('Please enter password');\n" +
                "                document.getElementById('password').focus();\n" +
                "            } else if (document.getElementById('captcha').value=='') {\n" +
                "                alert('Please enter captcha');document.getElementById('captcha').focus();\n" +
                "            } else {\n" +
                "                document.getElementById('billGenerator').submit();\n" +
                "            }\n" +
                "        }\n" +
                "    \n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"card\">\n" +
                "    <div>\n" +
                "        <h3 class=\"h1\">GENERATE DEFECTIVE LIST</h3>\n" +
                "        <p class=\"line\"></p>\n" +
                "        <form name=\"billGenerator\" id=\"billGenerator\" method=\"POST\" action=\"/app/v2/defects/login\">\n" +
                "            <div class=\"container\">\n" +
                "                <label class=\"label\"><b> <i class=\"fa fa-user\"></i> Username</i> </b></label><br>\n" +
                "                <input class=\"box\" id=\"username\" name=\"username\" type=\"text\" placeholder=\"Enter Username\"><br>\n" +
                "                <label class=\"label\"><b><i class=\"fa fa-key\"></i> Password</b></label><br>\n" +
                "                <input class=\"box\" type=\"password\" placeholder=\"Enter Password\" id=\"password\" name=\"password\"><br>\n" +
                "                <label class=\"label\"><i class=\"fa fa-reddit-alien\"></i> <b>Captcha</b></label><br>\n" +
                "                <input class=\"box\" id=\"captcha\" type=\"captcha\" id=\"captcha\" placeholder=\"Enter Captcha From Below\" name=\"captcha\"><br><br>\n" +
                "                <img src=\"" + captchaUrl + "\">\n" +
                "                <br>\n" +
                "                <input class='check' type=\"checkbox\" name=\"includeOther\"> INCLUDE OTHERS <br>\n" +
                "                <input class='check' type=\"checkbox\" name=\"showOnlyNumbers\"> VIEW COMPLAINT NUMBERS<br>\n" +
                "                <input type=\"hidden\" name=\"server\" id=\"serverId\" value=\"" + serverId + "\">\n" +
                "                <input type=\"hidden\" name=\"id\" id=\"sessionId\" value=\"" + jSessionId + "\">\n" +
                "                <button class='submit' type=button onclick=javascript:validate()>Login</button>\n" +
                "\n" +
                "                <label><br>\n" +
                "\n" +
                "                </label></div>\n" +
                "    </div>\n" +
                "    </form>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getLoginPageV3(final String jSessionId, final String serverId, final URL captchaUrl) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "@import url('https://fonts.googleapis.com/css?family=Raleway:400,700');\n" +
                "\n" +
                "* {\n" +
                "\tbox-sizing: border-box;\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\t\n" +
                "\tfont-family: Raleway, sans-serif;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "\tbackground: linear-gradient(90deg, #C7C5F4, #776BCC);\t\t\n" +
                "}\n" +
                "\n" +
                ".container {\n" +
                "\tdisplay: flex;\n" +
                "\talign-items: center;\n" +
                "\tjustify-content: center;\n" +
                "\tmin-height: 100vh;\n" +
                "}\n" +
                "\n" +
                ".screen {\t\t\n" +
                "\tbackground: linear-gradient(90deg, #5D54A4, #7C78B8);\t\t\n" +
                "\tposition: relative;\t\n" +
                "\theight: 500px;\n" +
                "\twidth: 360px;\t\n" +
                "\tbox-shadow: 0px 0px 24px #5C5696;\n" +
                "}\n" +
                "\n" +
                ".screen__content {\n" +
                "\tz-index: 1;\n" +
                "\tposition: relative;\t\n" +
                "}\n" +
                "\n" +
                ".screen__background {\t\t\n" +
                "\tposition: absolute;\n" +
                "\ttop: 0;\n" +
                "\tleft: 0;\n" +
                "\tright: 0;\n" +
                "\tbottom: 0;\n" +
                "\tz-index: 0;\n" +
                "\t-webkit-clip-path: inset(0 0 0 0);\n" +
                "\tclip-path: inset(0 0 0 0);\t\n" +
                "}\n" +
                "\n" +
                ".screen__background__shape {\n" +
                "\ttransform: rotate(45deg);\n" +
                "\tposition: absolute;\n" +
                "}\n" +
                "\n" +
                ".screen__background__shape1 {\n" +
                "\theight: 520px;\n" +
                "\twidth: 520px;\n" +
                "\tbackground: #FFF;\t\n" +
                "\ttop: -50px;\n" +
                "\tright: 120px;\t\n" +
                "\tborder-radius: 0 72px 0 0;\n" +
                "}\n" +
                "\n" +
                ".screen__background__shape2 {\n" +
                "\theight: 220px;\n" +
                "\twidth: 220px;\n" +
                "\tbackground: #6C63AC;\t\n" +
                "\ttop: -172px;\n" +
                "\tright: 0;\t\n" +
                "\tborder-radius: 32px;\n" +
                "}\n" +
                "\n" +
                ".screen__background__shape3 {\n" +
                "\theight: 540px;\n" +
                "\twidth: 190px;\n" +
                "\tbackground: linear-gradient(270deg, #5D54A4, #6A679E);\n" +
                "\ttop: -24px;\n" +
                "\tright: 0;\t\n" +
                "\tborder-radius: 32px;\n" +
                "}\n" +
                "\n" +
                ".screen__background__shape4 {\n" +
                "\theight: 400px;\n" +
                "\twidth: 200px;\n" +
                "\tbackground: #7E7BB9;\t\n" +
                "\ttop: 420px;\n" +
                "\tright: 50px;\t\n" +
                "\tborder-radius: 60px;\n" +
                "}\n" +
                "\n" +
                ".login {\n" +
                "\twidth: 320px;\n" +
                "\tpadding: 30px;\n" +
                "\tpadding-top: 10%;\n" +
                "}\n" +
                "\n" +
                ".login__field {\n" +
                "\tpadding: 5px 0px;\t\n" +
                "\tposition: relative;\t\n" +
                "}\n" +
                "\n" +
                ".login__icon {\n" +
                "\tposition: absolute;\n" +
                "\ttop: 0px;\n" +
                "\tcolor: #7875B5;\n" +
                "}\n" +
                "\n" +
                ".login__input {\n" +
                "\tborder: none;\n" +
                "\tborder-bottom: 2px solid #D1D1D4;\n" +
                "\tbackground: none;\n" +
                "\tpadding: 10px;\n" +
                "\tpadding-left: 24px;\n" +
                "\tfont-weight: 700;\n" +
                "\twidth: 75%;\n" +
                "\ttransition: .2s;\n" +
                "}\n" +
                "\n" +
                ".login__input:active,\n" +
                ".login__input:focus,\n" +
                ".login__input:hover {\n" +
                "\toutline: none;\n" +
                "\tborder-bottom-color: #6A679E;\n" +
                "}\n" +
                "\n" +
                ".login__submit {\n" +
                "\tbackground: #fff;\n" +
                "\tfont-size: 14px;\n" +
                "\tmargin-left: 45%;" +
                "\tmargin-top: 30px;\n" +
                "\tpadding: 16px 20px;\n" +
                "\tborder-radius: 26px;\n" +
                "\tborder: 1px solid #D4D3E8;\n" +
                "\ttext-transform: uppercase;\n" +
                "\tfont-weight: 700;\n" +
                "\tdisplay: flex;\n" +
                "\talign-items: center;\n" +
                "\twidth: 34%;\n" +
                "\tcolor: #4C489D;\n" +
                "\tbox-shadow: 0px 2px 2px #5C5696;\n" +
                "\tcursor: pointer;\n" +
                "\ttransition: .2s;\n" +
                "}\n" +
                "\n" +
                ".login__submit:active,\n" +
                ".login__submit:focus,\n" +
                ".login__submit:hover {\n" +
                "\tborder-color: #6A679E;\n" +
                "\toutline: none;\n" +
                "}\n" +
                ".login__title {\n" +
                "\tbackground: #fff;\n" +
                "\tfont-size: 14px;\n" +
                "\tmargin-top: 0px;\n" +
                "\tpadding: 16px 20px;\n" +
                "\tborder: 1px solid #D4D3E8;\n" +
                "\ttext-transform: uppercase;\n" +
                "\tdisplay: flex;\n" +
                "\talign-items: center;\n" +
                "\tcolor: #4C489D;\n" +
                "\tcursor: pointer;\n" +
                "\ttransition: .2s;\n" +
                "}\n" +
                "\n" +
                ".button__icon {\n" +
                "\tfont-size: 24px;\n" +
                "\tmargin-left: auto;\n" +
                "\tcolor: #7875B5;\n" +
                "}\n" +
                "\n" +
                ".social-login {\t\n" +
                "\tposition: absolute;\n" +
                "\theight: 140px;\n" +
                "\twidth: 160px;\n" +
                "\ttext-align: center;\n" +
                "\tbottom: 0px;\n" +
                "\tright: 0px;\n" +
                "\tcolor: #fff;\n" +
                "}\n" +
                "\n" +
                ".social-icons {\n" +
                "\tdisplay: flex;\n" +
                "\talign-items: center;\n" +
                "\tjustify-content: center;\n" +
                "}\n" +
                "\n" +
                ".social-login__icon {\n" +
                "\tpadding: 20px 10px;\n" +
                "\tcolor: #fff;\n" +
                "\ttext-decoration: none;\t\n" +
                "\ttext-shadow: 0px 0px 8px #7875B5;\n" +
                "}\n" +
                "\n" +
                ".social-login__icon:hover {\n" +
                "\ttransform: scale(1.5);\t\n" +
                "}" +
                "</style>" +
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
                "</head>" +
                "<body>" +
                "<div class=\"container\">\n" +

                "\t<div class=\"screen\">\n" +

                "\t\t<div class=\"screen__content\">\n" +
                "<h3 class=login__title><center>Generate Defective List</center></h3>" +

                "\t\t\t<form class=\"login\" name='billGenerator' id='billGenerator' method='POST' action='/app/v2/defects/login'>\n" +
                "\t\t\t\t<div class=\"login__field\">\n" +
                "\t\t\t\t\t<i class=\"login__icon fas fa-user\">Username</i>\n" +
                "\t\t\t\t\t<input type=\"text\" id=\"username\" name=\"username\" class=\"login__input\">\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class=\"login__field\">\n" +
                "\t\t\t\t\t<i class=\"login__icon fas fa-lock\">Password</i>\n" +
                "\t\t\t\t\t<input type=\"password\" id=\"password\" name=\"password\" class=\"login__input\">\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class=\"login__field\">\n" +
                "\t\t\t\t\t<i class=\"login__icon fas fa-lock\">Captcha</i>\n" +
                "\t\t\t\t\t<input type=\"captcha\" id=\"captcha\" name=\"captcha\" class=\"login__input\" placeholder=\"\">\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div class=\"login__field\">\n" +
                "\t\t\t\t\t<img src=\"" + captchaUrl + "\">" +
                "\t\t\t\t</div>\n" +
                getStyles() +
                "\t\t\t\t<div class=\"login__field\">\n" +
                "\t\t\t\t\t<i class=\"fa-user\"></i>\n" +
                getSlider("includeOther") + "&nbsp;Include Others<br>" +
                "\t\t\t\t\t</div>" +
                "\t\t\t\t<div class=\"login__field\">\n" +
                "\t\t\t\t\t<i class=\"fa-user\"></i>\n" +
                getSlider("showOnlyNumbers") + "View Complaint Numbers<br>" +
                "\t\t\t\t\t</div>" +
                "<input type=hidden name=id id=sessionId value=" + jSessionId + ">" +
                "<input type=hidden name=server id=serverId value=" + serverId + ">" +
                "\t\t\t\t<button class=\"button login__submit\" name='Login' value='Login' type=button onclick=javascript:validate()>\n" +
                "\t\t\t\t\t<span class=\"button\">LogIn</span>\n" +
                "\t\t\t\t</button>\t\t\t\t\n" +
                "\t\t\t</form>\n" +
                "\t\t</div>\n" +
                "\t\t<div class=\"screen__background\">\n" +
                "\t\t\t<span class=\"screen__background__shape screen__background__shape4\"></span>\n" +
                "\t\t\t<span class=\"screen__background__shape screen__background__shape3\"></span>\t\t\n" +
                "\t\t\t<span class=\"screen__background__shape screen__background__shape2\"></span>\n" +
                "\t\t\t<span class=\"screen__background__shape screen__background__shape1\"></span>\n" +
                "\t\t</div>\t\t\n" +
                "\t</div>\n" +
                "</div>" +
                "</body>" +
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
}
