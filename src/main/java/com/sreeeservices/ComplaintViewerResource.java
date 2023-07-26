package com.sreeeservices;

import com.sreeeservices.configuration.CacheType;
import com.sreeeservices.connector.GlenCrmConnector;
import com.sreeeservices.connector.ServitiumCrmConnector;
import com.sreeeservices.httprequestheaders.ContentRequest;
import com.sreeeservices.mapper.HtmlToDtoMapper;
import com.sreeeservices.model.ButterflyResponse;
import com.sreeeservices.model.CaptchaResponse;
import com.sreeeservices.model.Company;
import com.sreeeservices.model.GlenResponse;
import com.sreeeservices.model.LoginRequest;
import com.sreeeservices.ui.ButterflyTable;
import com.sreeeservices.ui.ServitiumCaptchaLoginPage;
import com.sreeeservices.ui.GlenTable;
import com.sreeeservices.utils.RequestParameterResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/viewComplaints")
public class ComplaintViewerResource {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CAPTCHA = "captcha";
    private static final String J_SESSION_ID = "id";
    private static final String SERVER_NAME = "server";

    private static final String DELIMITER_SEMICOLON = ";";
    private static final String DELIMITER_COMMA = ",";

    private static final String JSESSION_ID_PREFIX = "JSESSIONID=";
    private static final String SERVER_ID_PREFIX = "PIL-VLB=";

    private final GlenCrmConnector glenCrmConnector;
    private final ServitiumCrmConnector servitiumCrmConnector;
    private final CacheService cacheService;


    @Autowired
    public ComplaintViewerResource(final GlenCrmConnector glenCrmConnector,
                                   final ServitiumCrmConnector servitiumCrmConnector,
                                   final CacheService cacheService) {
        this.glenCrmConnector = glenCrmConnector;
        this.servitiumCrmConnector = servitiumCrmConnector;
        this.cacheService = cacheService;
    }

    @GetMapping("/all")
    public String getAllContents(HttpServletRequest httpServletRequest) {
        return getContent() + getButterflyLoginPage(httpServletRequest);
    }

    @GetMapping("/glen")
    public String getContent() {
        List<GlenResponse> glenResponses = glenCrmConnector.getGlenResponse();

        return GlenTable.getTabledContent(glenResponses);
    }

    @GetMapping("/butterfly")
    public String getButterflyLoginPage(HttpServletRequest httpServletRequest) {
        return getServitiumLoginPage(httpServletRequest, Company.BUTTERFLY);
    }

    @PostMapping("/butterfly")
    public String getButterflyContent(@RequestParam Map<String, String> requestParams) {
        return getServitiumContent(requestParams, Company.BUTTERFLY);
    }

    @GetMapping("/symphony")
    public String getSymphonyLoginPage(HttpServletRequest httpServletRequest) {
        return getServitiumLoginPage(httpServletRequest, Company.SYMPHONY);
    }

    @PostMapping("/symphony")
    public String getSymphonyContent(@RequestParam Map<String, String> requestParams) {
        return getServitiumContent(requestParams, Company.SYMPHONY);
    }

    private String getServitiumLoginPage(HttpServletRequest httpServletRequest, Company company) {
        Optional<ContentRequest> contentRequest = Optional.empty();
        if (httpServletRequest != null && httpServletRequest.getCookies() != null) {
            contentRequest = Arrays.stream(httpServletRequest.getCookies())
                    .filter(cookie -> cookie.getName().equals("Session-" + company.name()))
                    .findFirst()
                    .map(cookie -> {
                        String cookieValue = cookie.getName().equals("Session-" + company.name()) ? cookie.getValue() : null;
                        if (cookieValue != null) {
                            return new ContentRequest(cookieValue.split("%3A")[0].trim(), cookieValue.split("%3A")[1].trim(), null);
                        }
                        return null;
                    });
        }
        if (contentRequest.isEmpty()) {
            return getCaptchaImage(company);
        } else {
            final String htmlContent = servitiumCrmConnector.getAllocationList(contentRequest.get(), company);
            List<ButterflyResponse> responses = HtmlToDtoMapper.toButterflyResponse(htmlContent);
            return ButterflyTable.getTabledContent(responses, company);
        }
    }

    private String getServitiumContent(@RequestParam Map<String, String> requestParams,
                                       final Company company) {
        LoginRequest loginRequest = buildLoginRequest(requestParams, company);
        var httpRequest = servitiumCrmConnector.login(loginRequest, company);
        if (httpRequest.is2xxSuccessful()) {
            final ContentRequest contentRequest = ContentRequest.builder()
                    .jSessionId(loginRequest.getJSessionId())
                    .server(loginRequest.getServer())
                    .build();
            String htmlContent = servitiumCrmConnector.getAllocationList(contentRequest, company);
            List<ButterflyResponse> responses = HtmlToDtoMapper.toButterflyResponse(htmlContent);
            return ServitiumCaptchaLoginPage.setCookieInBrowser(contentRequest, company)
                    + ButterflyTable.getTabledContent(responses, company);
        }
        return "Login Failed";
    }

    private String getCaptchaImage(Company company) {
        final Optional<ContentRequest> contentRequest = cacheService.get(CacheType.SESSION.getCacheName(),
                "session-" + company, ContentRequest.class);
        if (contentRequest.isPresent()) {
            return ServitiumCaptchaLoginPage.getCaptchaPage(contentRequest.get().getImageBytes(),
                    contentRequest.get().getJSessionId(), contentRequest.get().getServer(), company);
        } else {
            CaptchaResponse captchaResponse = servitiumCrmConnector.getHttpHeaders(company);
            final List<String> cookies = captchaResponse.getHttpHeaders().getValuesAsList("set-cookie");

            final String jSessionId = cookies.get(0).substring(JSESSION_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];
            final String serverId = cookies.get(1).substring(SERVER_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];

            cacheService.put(CacheType.SESSION.getCacheName(), "session-" + company, new ContentRequest(jSessionId, serverId,
                    captchaResponse.getImageBytes()));
            return ServitiumCaptchaLoginPage.getCaptchaPage(captchaResponse.getImageBytes(), jSessionId, serverId, company);
        }
    }

    private LoginRequest buildLoginRequest(final Map<String, String> requestParams, final Company company) {
        return new LoginRequest("0",
                company.equals(Company.BUTTERFLY) ? "ASI7692" : "ASP17251",
                company.equals(Company.BUTTERFLY) ? "Bgmal@1234" : "Path@1234",
                RequestParameterResolver.getValue(requestParams, CAPTCHA),
                RequestParameterResolver.getValue(requestParams, J_SESSION_ID),
                RequestParameterResolver.getValue(requestParams, SERVER_NAME),
                null);
    }



}
