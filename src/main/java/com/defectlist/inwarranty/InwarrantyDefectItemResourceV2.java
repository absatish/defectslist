package com.defectlist.inwarranty;

import com.defectlist.inwarranty.email.EmailService;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.NoDataFoundException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import com.defectlist.inwarranty.exception.UnknownException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.ui.Banners;
import com.defectlist.inwarranty.ui.MessageType;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


@RestController
@RequestMapping("/app/v2/defects")
public class InwarrantyDefectItemResourceV2 {

    private static final Logger LOGGER = getLogger(InwarrantyDefectItemResourceV2.class);

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CAPTCHA = "captcha";
    private static final String J_SESSION_ID = "id";
    private static final String SERVER_NAME = "server";
    private static final String INCLUDE_OTHER = "includeOther";
    private static final String ON = "on";
    private static final String SHOW_ONLY_NUMBERS = "showOnlyNumbers";

    private static final String UNKNOWN_ERROR = "Unknown error occurred. Please try again in few seconds" +
            "<br>";

    private final InwarrantyDefectItemService inwarrantyDefectItemService;
    private final EmailService emailService;

    @Autowired
    public InwarrantyDefectItemResourceV2(final InwarrantyDefectItemService inwarrantyDefectItemService,
                                          final EmailService emailService) {
        this.inwarrantyDefectItemService = inwarrantyDefectItemService;
        this.emailService = emailService;
    }

    @GetMapping
    public String initialPage() {
        try {
            return inwarrantyDefectItemService.getPreload(Version.VERSION_2);
        } catch (final Exception exception) {
            return Banners.getMessageBanner(MessageType.ERROR, exception.getMessage());
        }
    }

    @GetMapping("/login")
    public String login() {
        return Banners.getMessageBanner(MessageType.WARNING, "Invalid Session. Please first login..!") + initialPage();
    }

    @PostMapping(path = "/login")
    public String login(@RequestParam final Map<String, String> requestParams) {
        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            validateLoginRequest(loginRequest);
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
            emailService.sendEmail("Prohibited User tried to login", "user tried to login : " + requestParams);
            return Banners.getMessageBanner(MessageType.WARNING, prohibitedUserTriedToLoginException.getMessage());
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception);
        }
    }

    private void validateLoginRequest(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        loginRequest.validate();
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
                Version.VERSION_2);
    }

    private String getInvalidLoginRequestResponse(final InvalidLoginRequestException invalidLoginRequestException) {
        return initialPage() + invalidLoginRequestException.getMessage();
    }

    private String getUnknownExceptionResponse(final Exception exception) {
        final String message = Arrays.stream(exception.getStackTrace())
                .limit(20)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("<br>"));
        emailService.sendEmail("Login failed", message);
        return Banners.getMessageBanner(MessageType.ERROR,
                UNKNOWN_ERROR + exception.getMessage() + "<br><center><a href=/app/v1/defects>Go back</a></center>");
    }

}
