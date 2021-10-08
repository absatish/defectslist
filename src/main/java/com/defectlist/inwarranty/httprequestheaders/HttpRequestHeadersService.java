package com.defectlist.inwarranty.httprequestheaders;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class HttpRequestHeadersService {

    public HttpEntity<String> getHttpEntityForServitiumCrm(final ContentRequest contentRequest, final int totalRecords) {
        final HttpHeaders headers = new HttpHeaders();
        final String defectiveItemsFormData = "product=0&model=&wStatus=0&technician=0&part_no=&JobSheet=&bin1=DEF&REFRESH=&remarks=&hidFlag=TRUE&RecordCount=15&editFlag=T&SortBy=null&SortDir=null&LastSortBy=&LastSortDir=&ReSort=&FindI=&AllRecs=yes&txtPageSize=" + totalRecords;
        headers.add("Cookie", "JSESSIONID=" + contentRequest.getJSessionId() + "; SERVERID=" + contentRequest.getServer());
        headers.add("content-type", "application/x-www-form-urlencoded");
        headers.add("Host","butterfly.servitiumcrm.com");
        headers.add("Origin","https://butterfly.servitiumcrm.com");
        headers.add("Referer","https://butterfly.servitiumcrm.com/butterfly/rentalreturn.report?xmlFileName=rentalreturn_config.xml");
        headers.add("Connection", "keep-alive");
        headers.add("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8,hi;q=0.7,te;q=0.6");
        return new HttpEntity<String>(defectiveItemsFormData, headers);
    }

    public HttpEntity<String> getHttpEntityForLogin(final LoginRequest login) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded");
        headers.add("Host","butterfly.servitiumcrm.com");
        headers.add("Origin","https://butterfly.servitiumcrm.com");
        headers.add("Referer","https://butterfly.servitiumcrm.com/butterfly/index.jsp");
        headers.add("Cookie", "JSESSIONID=" + login.getJSessionId() + "; SERVERID=" + login.getServer());
        headers.add("Connection", "keep-alive");
        headers.add("Accept-Language", "en-US,en;q=0.9");
        return new HttpEntity<String>(login.getCredentialsAndCaptcha(), headers);
    }

}
