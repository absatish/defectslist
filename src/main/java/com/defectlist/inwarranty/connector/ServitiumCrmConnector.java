package com.defectlist.inwarranty.connector;

import com.defectlist.inwarranty.httprequestheaders.LogoutRequest;
import com.defectlist.inwarranty.model.CaptchaResponse;
import com.defectlist.inwarranty.httprequestheaders.ContentRequest;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import org.springframework.http.HttpStatus;

import javax.swing.text.Document;
import java.util.List;

public interface ServitiumCrmConnector {

    List<Document> getDetailsByComplaintNumbers(final List<String> complaintNumbers);

    String readContentFromServitiumCrm(final ContentRequest contentRequest);

    HttpStatus login(final LoginRequest loginRequest);

    CaptchaResponse getHttpHeaders();

    String getJobSheet(final String complaintId);

    void logout(final LogoutRequest logoutRequest);

}