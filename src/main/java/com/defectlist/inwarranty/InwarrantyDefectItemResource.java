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

    private static final String UNKNOWN_ERROR = "<font color=red>Unknown error occurred. Please try again in few seconds.</font>" +
            "<br>";

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
        return inwarrantyDefectItemService.getPreload();
    }

    @GetMapping("/login")
    public String login() {
        return "<center><font color=red>Invalid Session. Please first login..!</font></center><br>" + initialPage();
    }

    @PostMapping(path = "/login")
    public String login(@RequestParam final Map<String, String> requestParams) {
        try {
            final LoginRequest loginRequest = buildLoginRequest(requestParams);
            loginRequest.validate();
            return inwarrantyDefectItemService.login(loginRequest);
        } catch (final InvalidLoginRequestException invalidLoginRequestException) {
            return initialPage() + invalidLoginRequestException.getMessage();
        } catch (final NoDataFoundException noDataFoundException) {
            return "<hr><center><font color=green size=5px>" + noDataFoundException.getMessage()
                    + "</font></center></hr><br>";
        } catch (final ProhibitedUserTriedToLoginException prohibitedUserTriedToLoginException) {
            emailService.sendEmail("Prohibited User tried to login", "user tried to login : " + requestParams);
            return prohibitedUserTriedToLoginException.getMessage();
        } catch (final Exception exception) {
            final String message = Arrays.stream(exception.getStackTrace())
                    .limit(20)
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("<br>"));
            emailService.sendEmail("Login failed", message);
            return UNKNOWN_ERROR + exception.getMessage() + "<br><center><a href=/app/v1/defects>Go back</a></center>";
        }
    }

    private LoginRequest buildLoginRequest(final Map<String, String> requestParams) {
        return new LoginRequest("0",
                RequestParameterResolver.getValue(requestParams, USERNAME),
                RequestParameterResolver.getValue(requestParams, PASSWORD),
                RequestParameterResolver.getValue(requestParams, CAPTCHA),
                RequestParameterResolver.getValue(requestParams, J_SESSION_ID),
                RequestParameterResolver.getValue(requestParams, SERVER_NAME),
                RequestParameterResolver.getValue(requestParams, INCLUDE_OTHER).equalsIgnoreCase(ON));
    }

}
