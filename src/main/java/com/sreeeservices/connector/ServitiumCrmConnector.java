package com.sreeeservices.connector;

import com.sreeeservices.httprequestheaders.LogoutRequest;
import com.sreeeservices.httprequestheaders.ContentRequest;
import com.sreeeservices.model.CaptchaResponse;
import com.sreeeservices.model.Company;
import com.sreeeservices.model.LoginRequest;
import org.springframework.http.HttpStatus;

import javax.swing.text.Document;
import java.util.List;

public interface ServitiumCrmConnector {

    List<Document> getDetailsByComplaintNumbers(final List<String> complaintNumbers);

    String readContentFromServitiumCrm(final ContentRequest contentRequest);

    HttpStatus login(final LoginRequest loginRequest, final Company company);

    String getWelcomeList(final LoginRequest loginRequest);

    CaptchaResponse getHttpHeaders(final Company company);

    String getJobSheet(final String complaintId);

    void logout(final LogoutRequest logoutRequest);

    String getAllocationList(final ContentRequest contentRequest, final Company company);

}