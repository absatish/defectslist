package com.defectlist.inwarranty.httprequestheaders;

import com.defectlist.inwarranty.Version;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import lombok.*;

@Data
@Builder
public class LoginRequest {

    private static final String UNDEFINED = "undefined";

    public LoginRequest(final String mobile, final String userid, final String password, final String j_captcha_response,
                        final String jSessionId, final String server, final boolean printOthers,
                        final boolean showOnlyNumbers, final Version version) {
        this.mobile = mobile;
        this.userid = userid;
        this.password = password;
        this.j_captcha_response = j_captcha_response;
        this.jSessionId = jSessionId;
        this.server =server;
        this.printOthers = printOthers;
        this.showOnlyNumbers = showOnlyNumbers;
        this.version = version;
    }

    private final String mobile;

    private String userid;

    private final String password;

    private final String j_captcha_response;

    private final String jSessionId;

    private final String server;

    private final boolean printOthers;

    private final boolean showOnlyNumbers;

    private final Version version;

    public String getUserId() {
        return userid;
    }

    public boolean isShowOnlyNumbers() {
        return this.showOnlyNumbers;
    }

    public String getCredentialsAndCaptcha() {
        return "mobile="+mobile+"&userid="+userid+"&password="+password+"&j_captcha_response="+j_captcha_response;
    }

    public String getCaptcha() {
        return j_captcha_response;
    }

    public String getPassword() {
        return password;
    }

    public String getJSessionId() {
        return jSessionId;
    }

    public String getServer() {
        return server;
    }

    public boolean includeOthers() {
        return printOthers;
    }

    public Version getVersion() {
        return version;
    }

    public void validate() throws InvalidLoginRequestException {
        String errorMessage = "<script type=text/javascript>";
        final String originalText = "<script type=text/javascript>";

        validateUsername();


        if (UNDEFINED.equals(password) || password == null || password.isBlank() || password.isEmpty()) {
            errorMessage += "document.getElementById('error-password').innerHTML = '<font color=red size=1px>Password&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
        }
        if (UNDEFINED.equals(j_captcha_response) || j_captcha_response.isEmpty() || j_captcha_response.isBlank()) {
            errorMessage += "document.getElementById('error-captcha').innerHTML = '<font color=red size=1px>Captcha&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
        }
        if (!(errorMessage.equals(originalText))) {
            throw new InvalidLoginRequestException(errorMessage + "</script>");
        }
    }

    public void validateUsername() throws InvalidLoginRequestException {
        String errorMessage = "<script type=text/javascript>";
        final String originalText = "<script type=text/javascript>";
        if (UNDEFINED.equals(userid) || userid == null || userid.isEmpty() || userid.isBlank()) {
            errorMessage += "document.getElementById('error-username').innerHTML = '<font color=red size=1px>Username&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
        } else {
            userid = userid.replaceAll(" ", "");
        }
        if (!(UNDEFINED.equals(userid) || userid == null || userid.isBlank() || userid.isBlank()) && !("ASP8568".equalsIgnoreCase(userid) || "ASI7953".equalsIgnoreCase(userid))) {
            throw new ProhibitedUserTriedToLoginException("No Content Available to show");
        }
        if (!(errorMessage.equals(originalText))) {
            throw new InvalidLoginRequestException(errorMessage + "</script>");
        }
    }

}
