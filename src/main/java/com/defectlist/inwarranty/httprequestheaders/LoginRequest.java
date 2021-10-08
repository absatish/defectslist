package com.defectlist.inwarranty.httprequestheaders;

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

}
