package com.defectlist.inwarranty.resource;

import com.defectlist.inwarranty.InwarrantyDefectItemServiceV3;
import com.defectlist.inwarranty.Version;
import com.defectlist.inwarranty.email.EmailService;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.NoDataFoundException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.model.DefectivePartType;
import com.defectlist.inwarranty.model.GridItem;
import com.defectlist.inwarranty.model.LoginPageInfo;
import com.defectlist.inwarranty.model.LoginResponse;
import com.defectlist.inwarranty.ui.LineImage;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/app/v3/defects")
public class InwarrantyDefectItemResourceV3 {

    private static final Logger LOGGER = getLogger(InwarrantyDefectItemResourceV3.class);

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

    private final InwarrantyDefectItemServiceV3 inwarrantyDefectItemService;
    private final EmailService emailService;

    @Autowired
    public InwarrantyDefectItemResourceV3(final InwarrantyDefectItemServiceV3 inwarrantyDefectItemService,
                                          final EmailService emailService) {
        this.inwarrantyDefectItemService = inwarrantyDefectItemService;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<LoginPageInfo> initialPage() {
        try {
            return makeResponseEntity(inwarrantyDefectItemService.getPreload());
        } catch (final Exception exception) {
            return makeResponseEntity(LoginPageInfo.builder()
                    .errorMessage(exception.getMessage())
                    .build());
        }
    }

    @GetMapping("/login")
    public ResponseEntity<LoginPageInfo> login() {
        return initialPage();
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestParam final Map<String, String> requestParams) {
        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            validateLoginRequest(loginRequest);
            final LoginResponse content;
            if (loginRequest.isShowOnlyNumbers()) {
                content = inwarrantyDefectItemService.loginAndLoadBillNumbers(loginRequest);
            } else {
                content = inwarrantyDefectItemService.login(loginRequest);
            }
            return makeResponseEntity(content);
        } catch (final InvalidLoginRequestException | NoDataFoundException exception) {
            return getLoginResponseForException(exception);
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            emailService.sendEmail("Prohibited User tried to login", "user tried to login : " + requestParams);
            return getLoginResponseForException(prohibitedUserTriedToLoginException);
        } catch (final Exception exception) {
            return getUnknownExceptionResponse(exception);
        }
    }

    @GetMapping("/grid-item/{complaint-id}")
    public ResponseEntity<GridItem> getGridItem3(@PathVariable("complaint-id") final String complaintId,
                                                 @RequestParam(name = "loggedInUser", required = false) final String loggedInUser) {
        try {
            return makeResponseEntity(inwarrantyDefectItemService.getJobSheet(DefectivePartType.ARMATURE.name(),
                    complaintId, loggedInUser));
        } catch (final Exception exception) {
            return makeResponseEntity(GridItem.builder()
                    .complaintNumber(complaintId)
                    .errorMessage(exception.getMessage())
                    .build());
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

    private ResponseEntity<LoginResponse> getLoginResponseForException(final Exception exception) {
        var loginPageInfo = login();
        return makeResponseEntity(LoginResponse.builder()
                .loginSuccess(false)
                .errorMessage("Something went wrong while processing your request")
                .sessionId(loginPageInfo.getBody().getSessionId())
                .severId(loginPageInfo.getBody().getServerId())
                .captchaImageUrl(loginPageInfo.getBody().getCaptchaImageUrl())
                .build());
    }

    private ResponseEntity<LoginResponse> getUnknownExceptionResponse(final Exception exception) {
        final String message = Arrays.stream(exception.getStackTrace())
                .limit(20)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("<br>"));
        emailService.sendEmail("Login failed", message);
        return getLoginResponseForException(exception);
    }

    private <T> ResponseEntity<T> makeResponseEntity(final T body) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "https://winter-citizen-328416.el.r.appspot.com");
        headers.set("Access-Control-Allow-Methods", "GET, POST");
        headers.set("Access-Control-Allow-Headers", "Content-Type");
        return new ResponseEntity<T>(body, headers, HttpStatus.OK.value());
    }

}
