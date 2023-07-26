package com.sreeeservices.httprequestheaders;

import com.sreeeservices.model.Company;
import com.sreeeservices.model.GlenLoginRequest;
import com.sreeeservices.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HttpRequestHeadersService {

    private static final String HOST = "butterfly.servitiumcrm.com";
    private static final String ORIGIN = "https://" + HOST;
    private static final String REFERER = ORIGIN + "/butterfly/rentalreturn.report?xmlFileName=rentalreturn_config.xml";
    private static final String REFERER_ALLOCATION = ORIGIN + "/butterfly/callmanagement/testshowassignDSC.report?xmlFileName=testshowassignDSC_config.xml";
    private static final String KEEP_ALIVE = "keep-alive";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private final String butterflyAllocateItemsFormData;
    private final String symphonyAllocateItemsFormData;

    @Autowired
    public HttpRequestHeadersService(@Value("${servitium.butterfly.query-allocation}") final String butterflyAllocateItemsFormData,
                                     @Value("${servitium.symphony.query-allocation}") final String symphonyAllocateItemsFormData) {
        this.butterflyAllocateItemsFormData = butterflyAllocateItemsFormData;
        this.symphonyAllocateItemsFormData = symphonyAllocateItemsFormData;
    }

    public HttpEntity<String> getHttpEntityForServitiumCrm(final ContentRequest contentRequest, final int totalRecords) {
        final HttpHeaders headers = new HttpHeaders();
        final String defectiveItemsFormData = "product=0&model=&wStatus=0&technician=0&part_no=&JobSheet=&bin1=DEF&REFRESH=&remarks=&hidFlag=TRUE&RecordCount=15&editFlag=T&SortBy=null&SortDir=null&LastSortBy=&LastSortDir=&ReSort=&FindI=&AllRecs=yes&txtPageSize=" + totalRecords;
        addCommonHeaders(headers);
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + contentRequest.getJSessionId() + "; PIL-VLB=" + contentRequest.getServer());
        headers.add(HttpHeaders.REFERER, REFERER);
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en-GB,en-US;q=0.9,en;q=0.8,hi;q=0.7,te;q=0.6");
        return new HttpEntity<>(defectiveItemsFormData, headers);
    }

    public HttpEntity<String> getHttpEntityForButterfly(final ContentRequest contentRequest,
                                                        final Company company, final int totalRecords) {
        final HttpHeaders headers = new HttpHeaders();
        final LocalDate localDate = LocalDate.now();
        final String day = (localDate.getDayOfMonth() < 10 ? "0" : "") + localDate.getDayOfMonth();
        final String month = (localDate.getMonthValue() < 10 ? "0" : "") + localDate.getMonthValue();
        final String year = String.valueOf(localDate.getYear());
        final String allocateItemsFormData = company.equals(Company.BUTTERFLY) ? butterflyAllocateItemsFormData : symphonyAllocateItemsFormData;
        final String modifiedPayload = allocateItemsFormData.replaceAll("\\{DATE}", String.valueOf(localDate))
                .replaceAll("\\{DAY}", day)
                .replaceAll("\\{MONTH}", month)
                .replaceAll("\\{YEAR}", year)
                .replaceAll("\\{PAGESIZE}", "" + totalRecords);

        addCommonHeaders(headers);
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + contentRequest.getJSessionId() + "; PIL-VLB=" + contentRequest.getServer());
        return new HttpEntity<>(modifiedPayload, headers);
    }

    public HttpEntity<String> getHttpHeader(final String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return new HttpEntity<>(headers);
    }

    public HttpEntity<GlenLoginRequest> getHttpLoginCreds(final String username, final String password) {
        final HttpHeaders headers = new HttpHeaders();
        final GlenLoginRequest loginRequest = new GlenLoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return new HttpEntity<>(loginRequest, headers);
    }


    public HttpEntity<String> getHttpEntityForLogin(final LoginRequest login) {
        final HttpHeaders headers = new HttpHeaders();
        addCommonHeaders(headers);
        headers.add(HttpHeaders.REFERER, ORIGIN + "/butterfly/index.jsp");
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + login.getJSessionId() + "; PIL-VLB=" + login.getServer());
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
        headers.add(HttpHeaders.CONTENT_LENGTH, "69210");
        headers.add(HttpHeaders.CONNECTION,  KEEP_ALIVE);
    }

    private String getButterflyAllocationQuery() {
        return " and CAM.TECHNICIAN>=0 and CAM.asc_code = decode('0','0',CAM.ASC_CODE, '0') and CAM.asc_code = DECODE('8369','0','%','8369') and (cam.asc_code in (select uam.asc_code from pcs_user_access_mapping uam where cam.asc_code=uam.asc_code and uam.user_id='32605' ) or 2=(case when 1 not in (select 1 from pcs_user_access_mapping uam where uam.user_id='32605') then 2 else 1 end ))";
    }

}
