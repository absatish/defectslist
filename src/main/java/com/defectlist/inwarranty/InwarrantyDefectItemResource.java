package com.defectlist.inwarranty;

import com.defectlist.inwarranty.email.EmailService;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.NoDataFoundException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


@RestController
@RequestMapping("/app/v1/defects")
public class InwarrantyDefectItemResource {

    private static final Logger LOGGER = getLogger(InwarrantyDefectItemResource.class);

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CAPTCHA = "captcha";
    private static final String J_SESSION_ID = "id";
    private static final String SERVER_NAME = "server";
    private static final String INCLUDE_OTHER = "includeOther";
    private static final String ON = "on";
    private static final String SHOW_ONLY_NUMBERS = "showOnlyNumbers";

    private static final String UNKNOWN_ERROR = "<font color=red>Unknown error occurred. Please try again in few seconds.</font>" +
            "<br>";

    private static final String DEPRICATION_TEXT = "<br><Br><center><font color=red size=5px>This page has been upgraded to version-2. Please click on the following link to get redirected<br><hr>ఈ పేజీ  నవీనీకరించబడింది.  దయచేసి ఈ క్రింది వెర్షన్ - 2 లింక్ క్లిక్ చేయగలరు" +
            "<br><hr><a href=/app/v2/defects/>Version-2 Link</a></font></center>";

    private final InwarrantyDefectItemService inwarrantyDefectItemService;
    private final EmailService emailService;

    @Autowired
    public InwarrantyDefectItemResource(final InwarrantyDefectItemService inwarrantyDefectItemService,
                                        final EmailService emailService) {
        this.inwarrantyDefectItemService = inwarrantyDefectItemService;
        this.emailService = emailService;
    }

    @GetMapping
    public String initialPage() {
        return DEPRICATION_TEXT;
        //return inwarrantyDefectItemService.getPreload(Version.VERSION_1);
    }

    @GetMapping("/login")
    public String login() {
        return "<center><font color=red>Invalid Session. Please first login..!</font></center><br>" + initialPage();
    }

    @PostMapping(path = "/login")
    public String login(@RequestParam final Map<String, String> requestParams) {
        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            validateLoginRequest(loginRequest);
            if (loginRequest.isShowOnlyNumbers()) {
                return inwarrantyDefectItemService.loginAndLoadBillNumbers(loginRequest);
            }
            return inwarrantyDefectItemService.login(loginRequest);
        } catch (final InvalidLoginRequestException invalidLoginRequestException) {
            return getInvalidLoginRequestResponse(invalidLoginRequestException);
        } catch (final NoDataFoundException noDataFoundException) {
            return "<hr><center><font color=green size=5px>" + noDataFoundException.getMessage()
                    + "</font></center></hr><br>";
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            emailService.sendEmail("Prohibited User tried to login", "user tried to login : " + requestParams);
            return prohibitedUserTriedToLoginException.getMessage();
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
                Version.VERSION_1);
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
        return UNKNOWN_ERROR + exception.getMessage() + "<br><center><a href=/app/v1/defects>Go back</a></center>";
    }

}
