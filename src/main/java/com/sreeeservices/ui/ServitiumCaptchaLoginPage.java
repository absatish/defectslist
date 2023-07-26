package com.sreeeservices.ui;

import com.sreeeservices.httprequestheaders.ContentRequest;
import com.sreeeservices.model.Company;
import org.apache.logging.log4j.util.Base64Util;

import java.net.URL;
import java.util.Arrays;
import java.util.Base64;

public class ServitiumCaptchaLoginPage {

    public static String getCaptchaPage(final byte[] byteCode, final String jSessionId, final String serverId,
                                        final Company company) {
        String base64Image = Base64.getEncoder().encodeToString(byteCode);
        return "<html>" +
                "   <head>" +
                "       <title>Complaint Viewer Login Page</title>" +
                getHeaderV4() +
                "<script type=text/javascript>" +
                "function validate() {\n" +
                "   var userPrompt = true;\n" +
                "  if (document.getElementById('captcha').value=='') { \n" +
                "          alert('Please enter captcha');\n" +
                "          document.getElementById('captcha').focus();\n" +
                "          userPrompt=false;\n" +
                "   } " +
                "   return userPrompt;       \n" +
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
                "   captchaValue = '';\n" +
                "   captcha.forEach(prepareCaptcha);\n" +
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
                "       <form name='billGenerator' id='billGenerator' method='POST' onsubmit=\"return validate();\" action='/viewComplaints/" + company.name().toLowerCase() + "'>\n" +
                "       <div class=\"imgcontainer\">\n" +
                "       <img src=\"https://www.w3schools.com/howto/img_avatar2.png\" alt=\"Avatar\" class=\"avatar\">\n" +
                "       </div>\n" +
                "       <div class=\"container\">\n" +
                "       <label for=\"captcha\"><b>Captcha</b></label>\n" +
                "       <table width=100%><tr>" +
                "           <td><img id=\"ItemPreview\" src=\"data:image/png;base64," + base64Image + "\"></td>" +
                "           <td width=24%><input onkeyup=\"next(2)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha1\" id=\"captcha1\" required></td>" +
                "           <td width=24%><input onkeyup=\"next(3)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha2\" id=\"captcha2\" required></td>" +
                "           <td width=24%><input onkeyup=\"next(4)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha3\" id=\"captcha3\" required></td>" +
                "           <td width=24%><input onkeyup=\"next(5)\" max=\"9\" min=\"0\" type=\"number\" name=\"captcha4\" id=\"captcha4\" required></td>" +
                "       </tr></table>\n" +
                "       <input type=\"hidden\" name=\"id\" value=\"" + jSessionId + "\">\n" +
                "       <input type=\"hidden\" id=\"captcha\" name=\"captcha\">\n" +
                "       <input type=\"hidden\" name=\"server\" value=\"" + serverId + "\">\n" +
                "       <button type=\"submit\">Login</button>\n" +
                "       </div>\n" +
                "       </form>" +
                "   </body>" +
                "</html>";
    }

    public static String setCookieInBrowser(final ContentRequest contentRequest, final Company company) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <script>\n" +
                "    function setCookie() {\n" +
                "      var cookieName = \"Session-" + company.name() + "\";\n" +
                "      var cookieValue = \"" + contentRequest.getJSessionId() + ":" + contentRequest.getServer() + "\";\n" +
                "       var date = new Date(Date.now() + 86400e3);" +
                "       date = date.toUTCString();" +
                "       var expires = \"; expires=date\"; // Set an expiration date for the cookie\n" +
                "\n" +
                "      // Create the cookie string\n" +
                "      var cookieString = encodeURIComponent(cookieName) + \"=\" + encodeURIComponent(cookieValue) + expires + \"; path=/\";\n" +
                "\n" +
                "      // Set the cookie\n" +
                "      document.cookie = cookieString;\n" +
                "\n" +
                "    }\n" +
                "  </script>\n" +
                "</head>\n" +
                "<body onload=\"setCookie()\">\n" +
                "Login Success" +
                "</body>\n" +
                "</html>\n";
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
