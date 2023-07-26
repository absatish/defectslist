package com.sreeeservices.connector;

import com.sreeeservices.aop.EmailServiceEnabled;
import com.sreeeservices.exception.NoDataFoundException;
import com.sreeeservices.httprequestheaders.LogoutRequest;
import com.sreeeservices.model.CaptchaResponse;
import com.sreeeservices.httprequestheaders.ContentRequest;
import com.sreeeservices.httprequestheaders.HttpRequestHeadersService;
import com.sreeeservices.model.Company;
import com.sreeeservices.model.LoginRequest;
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
    public String getAllocationList(final ContentRequest contentRequest, final Company company) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                getProperUrl(servitiumCrmUrlService.getAllocationUrl(), company),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForButterfly(contentRequest, company, 1),
                new ParameterizedTypeReference<>() {
                });
        final String body = exchange.getBody();
        if (body.contains("Data not found")) {
            throw new NoDataFoundException("No allocation bills found...!");
        }
        final int totalRecords = Integer.parseInt(body.substring(body.indexOf("Total Records :"), body.indexOf("<input type='hidden' size='3' maxlength = '5' name='txtPageSize'"))
                .replaceAll("Total Records :","").split("<")[0]
                .replaceAll("&nbsp;", "").trim());
        return getAllocationList(contentRequest, totalRecords, company);
    }

    private String getAllocationList(final ContentRequest contentRequest, final int totalRecords, final Company company) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                getProperUrl(servitiumCrmUrlService.getAllocationUrl(), company),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForButterfly(contentRequest, company, totalRecords),
                new ParameterizedTypeReference<String>() {
                });
        final String body = exchange.getBody();
        if (body.contains("Data not found")) {
            throw new NoDataFoundException("No allocation bills found...!");
        }
        return body;
    }

    @Override
    public String readContentFromServitiumCrm(final ContentRequest contentRequest) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                servitiumCrmUrlService.getRentalReturn(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForServitiumCrm(contentRequest, 0),
                new ParameterizedTypeReference<String>() {
                });
        final String body = exchange.getBody();
        if (body.contains("Data not found")) {
            throw new NoDataFoundException("No pending bills found.. Sukheebhava..!");
        }
        final int totalRecords = Integer.parseInt(body.substring(body.indexOf("<input type='hidden' name='RecordCount'"), body.indexOf("<input type='hidden' name='editFlag'"))
                .replaceAll("<input type='hidden' name='RecordCount'","").split(">")[0]
                .replaceAll("value=","")
                .replaceAll("'","")
                .replaceAll(" ", ""));
        return readContentFromServitiumCrm(contentRequest, totalRecords);
    }

    @Override
    @EmailServiceEnabled
    public HttpStatus login(final LoginRequest loginRequest, final Company company) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                getProperUrl(servitiumCrmUrlService.getLogon(), company),
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
    public CaptchaResponse getHttpHeaders(final Company company) {
        final ResponseEntity<byte []> exchange = restOperations.getForEntity(
                getProperUrl(servitiumCrmUrlService.getCaptchaImage(), company), byte[].class);
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

    private String readContentFromServitiumCrm(final ContentRequest contentRequest, final int totalRecords) {
        final ResponseEntity<String> exchange = restOperations.exchange(
                servitiumCrmUrlService.getRentalReturn(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpEntityForServitiumCrm(contentRequest, totalRecords),
                new ParameterizedTypeReference<String>() {
                });
        return exchange.getBody();
    }

    private String getProperUrl(final String url, final Company company) {
        return url.replace("{SERVITIUM_APP}", company.name().toLowerCase());
    }
}