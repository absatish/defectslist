//package com.sreeeservices.httprequestheaders;
//
//import com.sreeeservices.Version;
//import com.sreeeservices.exception.InvalidLoginRequestException;
//import com.sreeeservices.exception.ProhibitedUserTriedToLoginException;
//import lombok.*;
//
//@Builder
//public class LoginRequest {
//
//    private static final String UNDEFINED = "undefined";
//
//    private String mobile;
//
//    private String userid;
//
//    private String password;
//
//    private String j_captcha_response;
//
//    private String jSessionId;
//
//    private String server;
//
//    private boolean printOthers;
//
//    private boolean showOnlyNumbers;
//
//    private Version version;
//
//    private String loggedInUserName;
//
//    private boolean paginated;
//
//    public LoginRequest(String mobile, String userid, String password, String j_captcha_response,
//        String jSessionId, String server, boolean printOthers, boolean showOnlyNumbers, Version version,
//        String loggedInUserName, boolean paginated) {
//        this.mobile = mobile;
//        this.userid = userid;
//        this.password = password;
//        this.j_captcha_response = j_captcha_response;
//        this.jSessionId = jSessionId;
//        this.server = server;
//        this.printOthers = printOthers;
//        this.showOnlyNumbers = showOnlyNumbers;
//        this.version = version;
//        this.loggedInUserName = loggedInUserName;
//        this.paginated = paginated;
//    }
//
//    public LoginRequest() {
//
//    }
//
//    public String getUserId() {
//        return userid;
//    }
//
//    public void setUserid(final String userid) {
//        this.userid = userid;
//    }
//
//    public String getUserid() {
//        return userid;
//    }
//
//    public boolean isShowOnlyNumbers() {
//        return this.showOnlyNumbers;
//    }
//
//    public String getCredentialsAndCaptcha() {
//        return "mobile="+mobile+"&userid="+userid+"&password="+password+"&j_captcha_response="+j_captcha_response;
//    }
//
//    public String getCaptcha() {
//        return j_captcha_response;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public String getJSessionId() {
//        return jSessionId;
//    }
//
//    public String getServer() {
//        return server;
//    }
//
//    public boolean includeOthers() {
//        return printOthers;
//    }
//
//    public boolean paginated() {
//        return paginated;
//    }
//
//    public Version getVersion() {
//        return version;
//    }
//
//    public String getLoggedInUserName() {
//        return loggedInUserName;
//    }
//
//    public void setLoggedInUserName(final String loggedInUserName) {
//        this.loggedInUserName = loggedInUserName;
//    }
//
//    public void validate() throws InvalidLoginRequestException {
//        String errorMessage = "<script type=text/javascript>";
//        final String originalText = "<script type=text/javascript>";
//
//        validateUsername();
//
//        if (UNDEFINED.equals(password) || password == null || password.isBlank() || password.isEmpty()) {
//            errorMessage += "document.getElementById('error-password').innerHTML = '<font color=red size=1px>Password&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
//        }
//        if (UNDEFINED.equals(j_captcha_response) || j_captcha_response.isEmpty() || j_captcha_response.isBlank()) {
//            errorMessage += "document.getElementById('error-captcha').innerHTML = '<font color=red size=1px>Captcha&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
//        }
//        if (!(errorMessage.equals(originalText))) {
//            throw new InvalidLoginRequestException(errorMessage + "</script>");
//        }
//    }
//
//    public void validateUsername() throws InvalidLoginRequestException {
//        String errorMessage = "<script type=text/javascript>";
//        final String originalText = "<script type=text/javascript>";
//        if (UNDEFINED.equals(userid) || userid == null || userid.isEmpty() || userid.isBlank()) {
//            errorMessage += "document.getElementById('error-username').innerHTML = '<font color=red size=1px>Username&nbsp;cannot&nbsp;be&nbsp;empty</font>';\n";
//        } else {
//            userid = userid.replaceAll(" ", "");
//        }
//        if (!(UNDEFINED.equals(userid) || userid == null || userid.isBlank() || userid.isBlank()) && !("ASP8568".equalsIgnoreCase(userid) || "ASI7953".equalsIgnoreCase(userid))) {
//            throw new ProhibitedUserTriedToLoginException("Dear " + userid + ", Something went wrong.");
//        }
//        if (!(errorMessage.equals(originalText))) {
//            throw new InvalidLoginRequestException(errorMessage + "</script>");
//        }
//    }
//
//}
