package com.defectlist.inwarranty.connector;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ServitiumCrmUrlService {

    private static final String CAPTCHA_IMAGE = "CaptchaImg.jpg";
    private static final String LOGON = "logon.do";
    private static final String LOGOFF = "logoff.do";
    private static final String RENTAL_RETURN = "rentalreturn.report?xmlFileName=rentalreturn_config.xml";
    private static final String JOB_SHEET = "callmanagement/printJobSheet.do?callId={callId}";
    private static final String WELCOME_LIST = "welcomelist.jsp";
    private static final String CALL_SEARCH = "callmanagement/callsearchaction.do?flag=CallSearch";
    private static final String HAPPY_CODE = "callmanagement/callclose.do?type=refresh&callId={callId}&ORGID=0&COMPANYCD=304";
    private final String captchaImage;
    private final String logon;
    private final String rentalReturn;
    private final String jobSheet;
    private final String logout;
    private final String welcomeList;
    private final String callSearch;
    private final String happyCode;

    @Autowired
    public ServitiumCrmUrlService(@Value("${servitium.butterfly.url-prefix}") final String urlPrefix,
                                  @Value("${servitium.app.url-prefix}") final String appPrefix) {
        this.captchaImage = urlPrefix + CAPTCHA_IMAGE;
        this.logon = urlPrefix + LOGON;
        this.rentalReturn = urlPrefix + RENTAL_RETURN;
        this.jobSheet = appPrefix + JOB_SHEET;
        this.logout = urlPrefix + LOGOFF;
        this.welcomeList = urlPrefix + WELCOME_LIST;
        this.callSearch = urlPrefix + CALL_SEARCH;
        this.happyCode = urlPrefix + HAPPY_CODE;
    }

    public String getCaptchaImage() {
        return captchaImage;
    }

    public String getLogon() {
        return logon;
    }

    public String getRentalReturn() {
        return rentalReturn;
    }

    public String getJobSheet() {
        return jobSheet;
    }

    public String getLogout() {
        return logout;
    }

    public String getWelcomeList() {
        return welcomeList;
    }

    public String getCallSearch() {
        return callSearch;
    }

    public String getHappyCode() {
        return happyCode;
    }
}
