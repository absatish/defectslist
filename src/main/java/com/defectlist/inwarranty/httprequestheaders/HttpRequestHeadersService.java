package com.defectlist.inwarranty.httprequestheaders;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HttpRequestHeadersService {

    private static final String HOST = "butterfly.servitiumcrm.com";
    private static final String ORIGIN = "https://" + HOST;
    private static final String REFERER = ORIGIN + "/butterfly/rentalreturn.report?xmlFileName=rentalreturn_config.xml";
    private static final String KEEP_ALIVE = "keep-alive";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String CALL_SEARCH_FORM_DATA = "contxPath=/butterfly&ascFilter=0&orgId=0&companyCd=304&hidCallId=&hidStatus=&hidRepairEndDt=&hidEngineer=&hidIVRFl=&hidCallStatus=&hidcall_stage=&hidBrand=&viewUrl=&hidfgr=0&hidcn=0&hidprn=0&hidRowid=&userId=2271&hidMainCallId=&requestProcessMessage=&statusSelectMandatory=Y&fromdate=&regisdate1=%s&regisdate2=%s&regisdate3=%s&fromdate=&toregisdate1=%s&toregisdate2=%s&toregisdate3=%s&dateType=3&callId=&mainCallId=&mobileNo=&warranty=&brand=1&productSegment1=0&callType=&reminder=&status=2&discountGiven=0&genericName=&companytype=&technician=&branchCode=null&brCode=&pinCode1=&pinCode=&area1=0&city1=0&city=0&REFRESH=&REFRESH1=";
    private static final String DEFECTIVES_FORM_DATA = "product=0&model=&wStatus=0&technician=0&part_no=&JobSheet=&bin1=%s&REFRESH=&remarks=&hidFlag=TRUE&RecordCount=%s&editFlag=T&SortBy=null&SortDir=null&LastSortBy=&LastSortDir=&ReSort=&FindI=&AllRecs=yes&txtPageSize=%s";
    private static final String HAPPY_CODE = "ascFilter=0&orgId=0&companyCd=304&hidCallId={callId}&hidStatus=1&hidRepairEndDt=null&hidEngineer=Vikram+&hidIVRFl=T&hidCallStatus=Open&hidcall_stage=7&viewUrl=&hidBrand=1&hidRowid=1&dataSaved=&contxPath=%2Fbutterfly&CALLID={callId}&fromdate=&fromdate1={fromDate1}&fromdate2={fromDate2}&fromdate3={fromDate3}&rfromdate=&rfromdate1={fromDate1}&rfromdate2={fromDate2}&rfromdate3={fromDate3}&mainCallId=&fromdate=&deliveryDay=++++&deliveryMonth=&deliveryYear=&deliveryNumber=&branchCode=0&brCode=0&companytype=0&technician=0&state=0&city=0&city1=&callType=&REFRESH=&REFRESH1=&chkbox={callId}%231%23null%23Vikram+%23T%23Open%237%231%231&prodSeg=004&prodSegType=0048PD";

    public HttpEntity<String> getHttpEntityForServitiumCrm(final ContentRequest contentRequest, final int totalRecords, final String type) {
        final HttpHeaders headers = new HttpHeaders();
        final String formData;
        if (contentRequest.isCallSearch()) {
            final LocalDate fromDate = contentRequest.getFromDate();
            final LocalDate toDate = contentRequest.getToDate();
            formData = String.format(CALL_SEARCH_FORM_DATA, convert(fromDate.getDayOfMonth()),
                    convert(fromDate.getMonth().getValue()), fromDate.getYear(),
                    convert(toDate.getDayOfMonth()), convert(toDate.getMonth().getValue()),
                    toDate.getYear());
        } else {
            formData = String.format(DEFECTIVES_FORM_DATA, type, totalRecords, totalRecords);
        }
        addCommonHeaders(headers);
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + contentRequest.getJSessionId() + "; SERVERID=" + contentRequest.getServer());
        headers.add(HttpHeaders.REFERER, REFERER);
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en-GB,en-US;q=0.9,en;q=0.8,hi;q=0.7,te;q=0.6");
        return new HttpEntity<>(formData, headers);
    }

    public HttpEntity<String> getHttpEntityForHappyCode(final ContentRequest contentRequest, String callId) {
        final HttpHeaders headers = new HttpHeaders();
        //B24020602104
        final String fromDate3 = "20" + callId.substring(1, 3);
        final String fromDate2 = callId.substring(3, 5);
        final String fromDate1 = callId.substring(5, 7);

        final String happyCode = HAPPY_CODE.replaceAll("\\{fromDate1}", fromDate1)
                .replaceAll("\\{fromDate2}", fromDate2)
                .replaceAll("\\{fromDate3}", fromDate3)
                .replaceAll("\\{callId}", callId);

        addCommonHeaders(headers);
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + contentRequest.getJSessionId() + "; SERVERID=" + contentRequest.getServer());
        headers.add(HttpHeaders.REFERER, REFERER);
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, "en-GB,en-US;q=0.9,en;q=0.8,hi;q=0.7,te;q=0.6");
        return new HttpEntity<>(happyCode, headers);
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

    private String convert(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

}
