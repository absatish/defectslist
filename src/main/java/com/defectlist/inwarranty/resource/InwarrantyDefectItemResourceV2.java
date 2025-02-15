package com.defectlist.inwarranty.resource;

import com.defectlist.inwarranty.InwarrantyDefectItemService;
import com.defectlist.inwarranty.PaginatedInwarrantyDefectItemService;
import com.defectlist.inwarranty.Version;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.NoDataFoundException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.model.PaginatedRequest;
import com.defectlist.inwarranty.model.TargetPage;
import com.defectlist.inwarranty.ui.Banners;
import com.defectlist.inwarranty.ui.MessageType;
import com.defectlist.inwarranty.ui.UIFactory;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


@RestController
@RequestMapping("/app/v2/defects")
public class InwarrantyDefectItemResourceV2 {

    private static final Logger LOGGER = getLogger(InwarrantyDefectItemResourceV2.class);

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String COMPLAINT_IDS = "complaintIds";
    private static final String CAPTCHA = "captcha";
    private static final String J_SESSION_ID = "id";
    private static final String SERVER_NAME = "server";
    private static final String INCLUDE_OTHER = "includeOther";
    private static final String ON = "on";
    private static final String SHOW_ONLY_NUMBERS = "showOnlyNumbers";
    private static final String PAGINATED = "paginated";

    private static final String UNKNOWN_ERROR = "Error occurred. Reason : ";

    private final InwarrantyDefectItemService inwarrantyDefectItemService;

    private final ObjectMapper objectMapper;

    private final PaginatedInwarrantyDefectItemService paginatedInwarrantyDefectItemService;

    @Autowired
    public InwarrantyDefectItemResourceV2(final InwarrantyDefectItemService inwarrantyDefectItemService,
                                          final ObjectMapper objectMapper,
                                          final PaginatedInwarrantyDefectItemService paginatedInwarrantyDefectItemService) {
        this.inwarrantyDefectItemService = inwarrantyDefectItemService;
        this.paginatedInwarrantyDefectItemService = paginatedInwarrantyDefectItemService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping
    public String firstPage(@RequestParam(required = false) final TargetPage targetPage) {
        return UIFactory.getFirstPageV4(targetPage == null ? TargetPage.DEFECTIVE : targetPage);
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) final TargetPage targetPage) {
        return Banners.getMessageBanner(MessageType.WARNING, "Invalid Session. Please first login..!")
                + firstPage(targetPage == null ? TargetPage.DEFECTIVE : targetPage);
    }

    @GetMapping("/prelogin")
    public String preLoad(@RequestParam(required = false) final TargetPage targetPage) {
        return firstPage(targetPage == null ? TargetPage.DEFECTIVE : targetPage);
    }

    @PostMapping(path = "/prelogin")
    public String preLogin(@RequestParam final Map<String, String> requestParams,
                           @RequestParam(required = false) TargetPage targetPage) {
        targetPage = targetPage == null ? TargetPage.DEFECTIVE : targetPage;

        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            inwarrantyDefectItemService.validateUsername(loginRequest);
            return initialPage(loginRequest.getUserId(), targetPage);
        } catch (final InvalidLoginRequestException invalidLoginRequestException) {
            return getInvalidLoginRequestResponse(invalidLoginRequestException, targetPage);
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage()) + firstPage(targetPage);
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception) + firstPage(targetPage);
        }
    }

    @PostMapping(path = "/login")
    public String login(@RequestParam final Map<String, String> requestParams) {
        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            final String content;
            if (loginRequest.isShowOnlyNumbers()) {
                content = inwarrantyDefectItemService.loginAndLoadBillNumbers(loginRequest);
            } else {
                content = inwarrantyDefectItemService.login(loginRequest);
            }
            return Banners.getMessageBanner(MessageType.SUCCESS, "Hey..! " + loginRequest.getUserId() + " loggedIn successfully") + content;
        } catch (final InvalidLoginRequestException invalidLoginRequestException) {
            return getInvalidLoginRequestResponse(invalidLoginRequestException, TargetPage.DEFECTIVE);
        } catch (final NoDataFoundException noDataFoundException) {
            return Banners.getMessageBanner(MessageType.INFO, noDataFoundException.getMessage());
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage()) + firstPage(TargetPage.DEFECTIVE);
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception) + firstPage(TargetPage.DEFECTIVE);
        }
    }

    @PostMapping(path = "/login/next")
    public String nextPageContent(@RequestParam final Map<String, String> requestParams) {
        try {
            final PaginatedRequest paginatedRequest = buildPaginatedRequest(requestParams);
            final String content = paginatedInwarrantyDefectItemService.getPaginatedGridItems(paginatedRequest);
            return Banners.getMessageBanner(MessageType.SUCCESS, "Hey..! " + paginatedRequest.getLoggedInUserName() + " loggedIn successfully") + content;
        } catch (final NoDataFoundException noDataFoundException) {
            return Banners.getMessageBanner(MessageType.INFO, noDataFoundException.getMessage());
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage()) + firstPage(TargetPage.DEFECTIVE);
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception) + firstPage(TargetPage.DEFECTIVE);
        }
    }

    @GetMapping(path = "/grid-item")
    public String getGridItems(@RequestParam final Map<String, String> requestParams) {
        if (requestParams == null || requestParams.isEmpty() || requestParams.get("ids").isEmpty()) {
            return getBannerForGridItemInput();
        }
        try {
            final Map<String, String> complaintIds = new HashMap<>();
            complaintIds.put("OTHER", requestParams.get("ids"));

            final String content = paginatedInwarrantyDefectItemService.getPaginatedGridItems(complaintIds,
                    requestParams.getOrDefault("name", "VIKRAM SIVA KUMAR"), 1);
            return getBannerForGridItemInput() + content;
        } catch (final NoDataFoundException noDataFoundException) {
            return Banners.getMessageBanner(MessageType.INFO, noDataFoundException.getMessage());
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage())
                    + firstPage(TargetPage.DEFECTIVE);
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception) + firstPage(TargetPage.DEFECTIVE);
        }
    }

    @GetMapping("/good-items")
    public String getGoodItems() {
        return preLoad(TargetPage.GOOD);
    }

    @GetMapping("/start")
    public String uploadDoc(@RequestParam final Map<String, String> requestParams) {
        final LoginRequest loginRequest = buildLoginRequest(requestParams);
        return inwarrantyDefectItemService.uploadDoc(loginRequest);
    }

    @PostMapping("/good-items")
    public String getGoodItems(@RequestParam final Map<String, String> requestParams) {
        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            return inwarrantyDefectItemService.getGoodItems(loginRequest);
        } catch (Exception exception) {
            return getUnknownExceptionResponse(exception);
        }
    }

    @GetMapping("/happy-code")
    public String getHappyCodeGet(@RequestParam final Map<String, String> requestParams) {
        if (requestParams.isEmpty()) {
            return login(TargetPage.HAPPY_CODE);
        }
        return getHappyCode(requestParams);
    }

    @PostMapping("/happy-code")
    public String getHappyCode(@RequestParam final Map<String, String> requestParams) {
        final String callId = requestParams.get("callId");
        if (callId == null) {
            return getBannerForHappyCode(requestParams);
        }
        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            return getBannerForHappyCode(requestParams) + inwarrantyDefectItemService.getHappyCode(loginRequest, callId);
        } catch (Exception exception) {
            return getUnknownExceptionResponse(exception);
        }
    }

    private String initialPage(final String userId, final TargetPage targetPage) {
        try {
            String initialPage = inwarrantyDefectItemService.getPreload(Version.VERSION_2, targetPage);
            return initialPage.replaceAll("id=\"username\" name=\"username\"",
                    "id=\"username\" name=\"username\" readonly value=" + userId + " ")
                    .replaceAll("placeholder=username type=text id=username name=username",
                            "placeholder=username  readonly type=text id=username name=username value=" + userId + " ")
                    .replaceAll("<input id=\"username\" type=\"text\" placeholder=\"Enter Username\" name=\"username\" required>",
                            "<input id=\"username\" type=\"text\" readonly placeholder=\"Enter Username\" name=\"username\" required value=" + userId + ">");
        } catch (final Exception exception) {
            return Banners.getMessageBanner(MessageType.ERROR, exception.getMessage());
        }
    }

    private LoginRequest buildLoginRequest(final Map<String, String> requestParams) {
        return new LoginRequest("0",
                RequestParameterResolver.getValue(requestParams, USERNAME),
                RequestParameterResolver.getValue(requestParams, PASSWORD),
                RequestParameterResolver.getValue(requestParams, CAPTCHA),
                RequestParameterResolver.getValue(requestParams, J_SESSION_ID),
                RequestParameterResolver.getValue(requestParams, SERVER_NAME),
                RequestParameterResolver.getValue(requestParams, INCLUDE_OTHER).equalsIgnoreCase(ON),
                RequestParameterResolver.getValue(requestParams, SHOW_ONLY_NUMBERS).equalsIgnoreCase(ON),
                Version.VERSION_2,
                null,
                RequestParameterResolver.getValue(requestParams, PAGINATED).equalsIgnoreCase(ON)
                );
    }

    private PaginatedRequest buildPaginatedRequest(final Map<String, String> requestParams) throws JsonProcessingException {
        return new PaginatedRequest(RequestParameterResolver.getValue(requestParams, "loggedInUserName"),
                objectMapper.readValue(RequestParameterResolver.getValue(requestParams, COMPLAINT_IDS),
                        new TypeReference<Map<String, String>>() {
                        }),
                UUID.randomUUID(),
                Integer.parseInt(RequestParameterResolver.getValue(requestParams, "pageNumber")));
    }

    private String getInvalidLoginRequestResponse(final InvalidLoginRequestException invalidLoginRequestException,
                                                  final TargetPage targetPage) {
        return firstPage(targetPage) + invalidLoginRequestException.getMessage();
    }

    private String getUnknownExceptionResponse(final Exception exception) {
        final String message = Arrays.stream(exception.getStackTrace())
                .limit(20)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("<br>"));
        return Banners.getMessageBanner(MessageType.ERROR,
                UNKNOWN_ERROR + exception.getMessage());
    }

    private String getBannerForGridItemInput() {
        return Banners.getMessageBanner(MessageType.INFO,
                "<center><form name=extras type=get>" +
                        "Complaint Ids : <input class=input type=text name=ids> &nbsp;&nbsp;" +
                        "Technician Name : <input type=text name=name>&nbsp;&nbsp;" +
                        "Branch : <input type=text name=branch>&nbsp;&nbsp;&nbsp;" +
                        "<input type=submit></form></center>",
                false);
    }

    private String getBannerForHappyCode(Map<String, String> requestParams) {
        return Banners.getMessageBanner(MessageType.WARNING,
                "<center><form name=extras method='POST'>" +
                        "Complaint Ids : <input class=input type=text name=callId value=" + requestParams.get("callId") + ">  " +
                        requestParams.entrySet().stream()
                                .map(e -> "<input type=hidden name='" + e.getKey() + "' value='" + e.getValue() + "'>")
                                .collect(Collectors.joining(" ")) +
                        "<input type=submit></form></center>",
                false);
    }
}
