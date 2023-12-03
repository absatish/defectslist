package com.defectlist.inwarranty.connector;

import com.defectlist.inwarranty.aop.EmailServiceEnabled;
import com.defectlist.inwarranty.exception.NoDataFoundException;
import com.defectlist.inwarranty.httprequestheaders.LogoutRequest;
import com.defectlist.inwarranty.model.CaptchaResponse;
import com.defectlist.inwarranty.httprequestheaders.ContentRequest;
import com.defectlist.inwarranty.httprequestheaders.HttpRequestHeadersService;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import javax.swing.text.Document;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class ServitiumCrmAdapter implements ServitiumCrmConnector {

    private static final Logger LOGGER = getLogger(ServitiumCrmAdapter.class);

    private final HttpRequestHeadersService httpRequestHeadersService;

    private final RestOperations restOperations;

    private final ServitiumCrmUrlService servitiumCrmUrlService;

    @Autowired
    public ServitiumCrmAdapter(final HttpRequestHeadersService httpRequestHeadersService,
                               final RestOperations restOperations,
                               final ServitiumCrmUrlService servitiumCrmUrlService) {
        this.httpRequestHeadersService = httpRequestHeadersService;
        this.restOperations = restOperations;
        this.servitiumCrmUrlService = servitiumCrmUrlService;
    }

    @Override
    public List<Document> getDetailsByComplaintNumbers(List<String> complaintNumbers) {
        return null;
    }

    @Override
    public String readContentFromServitiumCrm(final ContentRequest contentRequest, String type) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                contentRequest.isCallSearch() ? servitiumCrmUrlService.getCallSearch() : servitiumCrmUrlService.getRentalReturn(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForServitiumCrm(contentRequest, 0, type),
                new ParameterizedTypeReference<String>() {
                });
        final String body = exchange.getBody();
        if (contentRequest.isCallSearch()) {
            return body;
        }
        if (body.contains("Data not found")) {
            throw new NoDataFoundException("No pending bills found.. Sukheebhava..!");
        }
        final int totalRecords = Integer.parseInt(body.substring(body.indexOf("<input type='hidden' name='RecordCount'"), body.indexOf("<input type='hidden' name='editFlag'"))
                .replaceAll("<input type='hidden' name='RecordCount'","").split(">")[0]
                .replaceAll("value=","")
                .replaceAll("'","")
                .replaceAll(" ", ""));
        return readContentFromServitiumCrm(contentRequest, totalRecords, type);
    }

    @Override
    @EmailServiceEnabled
    public HttpStatus login(final LoginRequest loginRequest) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                servitiumCrmUrlService.getLogon(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForLogin(loginRequest),
                new ParameterizedTypeReference<String>() {
                });
        if (Objects.requireNonNull(exchange.getBody()).contains("welcomelist.jsp")) {
            return HttpStatus.OK;
        }
        LOGGER.info("Login request failed. Content : {}", exchange.getBody());
        if (Objects.requireNonNull(exchange.getBody()).contains("Invalid Login Id or Password")) {
            throw new RuntimeException("Invalid Login Id or Password");
        }
        if (Objects.requireNonNull(exchange.getBody()).contains("Invalid Captcha Code")) {
            throw new RuntimeException("Invalid Captcha Code");
        }
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getWelcomeList(final LoginRequest loginRequest) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                servitiumCrmUrlService.getWelcomeList(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForLogin(loginRequest),
                new ParameterizedTypeReference<String>() {
                });
        if (Objects.requireNonNull(exchange.getBody()).contains("User Name")) {
            return exchange.getBody();
        }
        LOGGER.info("Get welcomelist request failed. Content : {}", exchange.getBody());
        return null;
    }

    @Override
    public CaptchaResponse getHttpHeaders() {
        final ResponseEntity<byte []> exchange = restOperations.exchange(
                servitiumCrmUrlService.getCaptchaImage(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });
        return new CaptchaResponse(exchange.getHeaders(), exchange.getBody());
    }

    @Override
    public String getJobSheet(final String complaintId) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                servitiumCrmUrlService.getJobSheet(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                },
                complaintId);
        return exchange.getBody();
    }

    @Async
    @Override
    public void logout(final LogoutRequest logoutRequest) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                servitiumCrmUrlService.getLogout(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForLogout(logoutRequest),
                new ParameterizedTypeReference<>() {
                });
        if (!HttpStatus.OK.equals(exchange.getStatusCode())) {
            LOGGER.error("Error while logging off.");
        }
    }

    public String readContentFromServitiumCrm(final ContentRequest contentRequest, final int totalRecords, final String type) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                servitiumCrmUrlService.getRentalReturn(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForServitiumCrm(contentRequest, totalRecords, type),
                new ParameterizedTypeReference<String>() {
                });
        return exchange.getBody();
    }

}