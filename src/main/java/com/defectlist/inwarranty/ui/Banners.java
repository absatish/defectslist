package com.defectlist.inwarranty.ui;

public class Banners {

    public static String getMessageBanner(final MessageType messageType, final String message, final boolean closeButton) {
        return "" +
                "<style>\n" +
                ".alert {\n" +
                "  padding: 20px;\n" +
                "  background-color: #f44336;\n" +
                "  color: white;\n" +
                "  opacity: 1;\n" +
                "  transition: opacity 0.6s;\n" +
                "  margin-bottom: 15px;\n" +
                "}\n" +
                "\n" +
                ".alert.success {background-color: #04AA6D;}\n" +
                ".alert.info {background-color: #2196F3;}\n" +
                ".alert.warning {background-color: #ff9800;}\n" +
                "\n" +
                ".closebtn {\n" +
                "  margin-left: 15px;\n" +
                "  color: white;\n" +
                "  font-weight: bold;\n" +
                "  float: right;\n" +
                "  font-size: 22px;\n" +
                "  line-height: 20px;\n" +
                "  cursor: pointer;\n" +
                "  transition: 0.3s;\n" +
                "}\n" +
                "\n" +
                ".closebtn:hover {\n" +
                "  color: black;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"alert " + messageType + "\">\n" +
                getCloseButton(closeButton) +
                "  <strong>" + message + "</strong>\n" +
                "</div>\n" +
                getCloseButtonScript(closeButton);
    }

    public static String getMessageBanner(final MessageType messageType, final String message) {
        return getMessageBanner(messageType, message, true);
    }

    private static String getCloseButtonScript(final boolean closeButtonRequired) {
        if (closeButtonRequired) {
            return  "<script>\n" +
                    "var close = document.getElementsByClassName(\"closebtn\");\n" +
                    "var i;\n" +
                    "\n" +
                    "for (i = 0; i < close.length; i++) {\n" +
                    "  close[i].onclick = function(){\n" +
                    "    var div = this.parentElement;\n" +
                    "    div.style.opacity = \"0\";\n" +
                    "    setTimeout(function(){ div.style.display = \"none\"; }, 600);\n" +
                    "  }\n" +
                    "}\n" +
                    "</script>";
        }
        return "";
    }

    private static String getCloseButton(final boolean closeButtonRequired) {
        return closeButtonRequired ? "  <span class=\"closebtn\">&times;</span>  \n" : "";
    }
}
