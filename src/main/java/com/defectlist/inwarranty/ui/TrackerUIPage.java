package com.defectlist.inwarranty.ui;

public class TrackerUIPage {

    public static String getFirstPage() {
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
                "} else if (document.getElementById('pin').value==''){" +
                "alert('Please enter pin');" +
                "document.getElementById('pin').focus();" +
                "} else {" +
                "document.getElementById('billGenerator').submit();" +
                "}}" +
                "</script>" +
                "\t</head>\n" +
                "\t<body>\n" +
                "<center><b><h4>Login page</h4></b></center><hr>" +
                "\t\t\t\t<div class=\"form-group\">" +
                "<form name='billGenerator' id='billGenerator' method='POST' action='/app/v1/tracker/login'>" +
                "<center>" +
                "<div class=\"container\" style=\"width:500px; align:center;\">\n" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Username\">Username</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-6\">  " +
                " <input class=form-control placeholder=username type=text id=username name=username></div>" +
                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-username></div>" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Pin\">Pin</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-6\">  " +
                "<input class=form-control placeholder=pin type=number id=pin name=pin></div>" +
                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-password></div>" +
                "\t\t\t\t<hr><div class=\"form-group\">\n" +
                "\t\t\t\t  <div class=\"col-sm-12\">  " +
                "<input class='btn btn-default' style='align:right;' name='Login' " +
                "value='Login' type=button onclick=javascript:validate()></div></div>" +
                "</table></center>" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }
}
