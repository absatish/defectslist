package com.defectlist.inwarranty.httprequestheaders;

public class LogoutRequest {

    public LogoutRequest(final String topLogin) {
        this.topLogin = topLogin;
    }

    private final String topLogin;

    public String getTopLogin() {
        return "toplogin=" + topLogin;
    }


}
