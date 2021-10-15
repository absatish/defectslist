package com.defectlist.inwarranty.httprequestheaders;

import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import lombok.*;

@Data
@Builder
public class LoginRequest {

    public LoginRequest(final String mobile, final String userid, final String password, final String j_captcha_response,
                        final String jSessionId, final String server) {
        this.mobile = mobile;
        this.userid = userid;
        this.password = password;
        this.j_captcha_response = j_captcha_response;
        this.jSessionId = jSessionId;
        this.server =server;
    }

    private final String mobile;

    private final String userid;

    private final String password;

    private final String j_captcha_response;

    private final String jSessionId;

    private final String server;

    public String getCredentialsAndCaptcha() {
        return "mobile="+mobile+"&userid="+userid+"&password="+password+"&j_captcha_response="+j_captcha_response;
    }

    public String getJSessionId() {
        return jSessionId;
    }

    public String getServer() {
        return server;
    }

    public void validate() throws InvalidLoginRequestException {
        String errorMessage = "<script type=text/javascript>";
        final String originalText = "<script type=text/javascript>";

        if (userid.isEmpty() || userid.isBlank()) {
            errorMessage += "document.getElementById('error-username').innerHTML = '<font color=red size=1px>Username&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
        }
        if (password.isBlank() || password.isEmpty()) {
            errorMessage += "document.getElementById('error-password').innerHTML = '<font color=red size=1px>Password&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
        }
        if (j_captcha_response.isEmpty() || j_captcha_response.isBlank()) {
            errorMessage += "document.getElementById('error-captcha').innerHTML = '<font color=red size=1px>Captcha&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
        }
        if (!(errorMessage.equals(originalText))) {
            throw new InvalidLoginRequestException(errorMessage + "</script>");
        }
    }

}
