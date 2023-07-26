package com.defectlist.inwarranty.httprequestheaders;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class HttpRequestHeadersService {

    private static final String HOST = "butterfly.servitiumcrm.com";
    private static final String ORIGIN = "https://" + HOST;
    private static final String REFERER = ORIGIN + "/butterfly/rentalreturn.report?xmlFileName=rentalreturn_config.xml";
    private static final String KEEP_ALIVE = "keep-alive";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    public HttpEntity<String> getHttpEntityForServitiumCrm(final ContentRequest contentRequest, final int totalRecords) {
        final HttpHeaders headers = new HttpHeaders();
        final String defectiveItemsFormData = "product=0&model=&wStatus=0&technician=0&part_no=&JobSheet=&bin1=DEF&REFRESH=&remarks=&hidFlag=TRUE&RecordCount=15&editFlag=T&SortBy=null&SortDir=null&LastSortBy=&LastSortDir=&ReSort=&FindI=&AllRecs=yes&txtPageSize=" + totalRecords;
        addCommonHeaders(headers);
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + contentRequest.getJSessionId() + "; SERVERID=" + contentRequest.getServer());
        headers.add(HttpHeaders.REFERER, REFERER);
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en-GB,en-US;q=0.9,en;q=0.8,hi;q=0.7,te;q=0.6");
        return new HttpEntity<>(defectiveItemsFormData, headers);
    }

    public HttpEntity<String> getHttpEntityForLogin(final LoginRequest login) {
        final HttpHeaders headers = new HttpHeaders();
        addCommonHeaders(headers);
        headers.add(HttpHeaders.REFERER, ORIGIN + "/butterfly/index.jsp");
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + login.getJSessionId() + "; SERVERID=" + login.getServer());
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.9");
        return new HttpEntity<>(login.getCredentialsAndCaptcha(), headers);
    }

    public HttpEntity<String> getHttpEntityForLogout(final LogoutRequest logoutRequest) {
        final HttpHeaders headers = new HttpHeaders();
        addCommonHeaders(headers);
        headers.add(HttpHeaders.REFERER, ORIGIN + "/butterfly/top.jsp");
        return new HttpEntity<>(logoutRequest.getTopLogin(), headers);
    }

    private void addCommonHeaders(final HttpHeaders headers) {
        headers.add(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        headers.add(HttpHeaders.HOST, HOST);
        headers.add(HttpHeaders.ORIGIN, ORIGIN);
        headers.add(HttpHeaders.CONNECTION,  KEEP_ALIVE);
    }

}
