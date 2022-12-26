package com.defectlist.inwarranty.resource;

import com.defectlist.inwarranty.InwarrantyDefectItemService;
import com.defectlist.inwarranty.PaginatedInwarrantyDefectItemService;
import com.defectlist.inwarranty.Version;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.NoDataFoundException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.model.PaginatedRequest;
import com.defectlist.inwarranty.ui.Banners;
import com.defectlist.inwarranty.ui.MessageType;
import com.defectlist.inwarranty.ui.UIFactory;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    public String firstPage() {
        return UIFactory.getFirstPageV4();
    }

    @GetMapping("/login")
    public String login() {
        return Banners.getMessageBanner(MessageType.WARNING, "Invalid Session. Please first login..!") + firstPage();
    }

    @GetMapping("/prelogin")
    public String preLoad() {
        return firstPage();
    }
    @PostMapping(path = "/prelogin")
    public String preLogin(@RequestParam final Map<String, String> requestParams) {

        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            inwarrantyDefectItemService.validateUsername(loginRequest);
            return initialPage(loginRequest.getUserId());
        } catch (final InvalidLoginRequestException invalidLoginRequestException) {
            return getInvalidLoginRequestResponse(invalidLoginRequestException);
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage()) + firstPage();
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception) + firstPage();
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
            return getInvalidLoginRequestResponse(invalidLoginRequestException);
        } catch (final NoDataFoundException noDataFoundException) {
            return Banners.getMessageBanner(MessageType.INFO, noDataFoundException.getMessage());
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage()) + firstPage();
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception) + firstPage();
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
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage()) + firstPage();
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception) + firstPage();
        }
    }

    private String initialPage(final String userId) {
        try {
            String initialPage = inwarrantyDefectItemService.getPreload(Version.VERSION_2);
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

    private String getInvalidLoginRequestResponse(final InvalidLoginRequestException invalidLoginRequestException) {
        return firstPage() + invalidLoginRequestException.getMessage();
    }

    private String getUnknownExceptionResponse(final Exception exception) {
        final String message = Arrays.stream(exception.getStackTrace())
                .limit(20)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("<br>"));
        return Banners.getMessageBanner(MessageType.ERROR,
                UNKNOWN_ERROR + exception.getMessage());
    }

}
