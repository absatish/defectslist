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
    private static final String RENTAL_RETURN = "rentalreturn.report?xmlFileName=rentalreturn_config.xml";
    private static final String JOB_SHEET = "callmanagement/printJobSheet.do?callId={callId}";

    private final String captchaImage;
    private final String logon;
    private final String rentalReturn;
    private final String jobSheet;

    @Autowired
    public ServitiumCrmUrlService(@Value("${servitium.butterfly.url-prefix}") final String urlPrefix,
                                  @Value("${servitium.app.url-prefix}") final String appPrefix) {
        this.captchaImage = urlPrefix + CAPTCHA_IMAGE;
        this.logon = urlPrefix + LOGON;
        this.rentalReturn = urlPrefix + RENTAL_RETURN;
        this.jobSheet = appPrefix + JOB_SHEET;
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
}
