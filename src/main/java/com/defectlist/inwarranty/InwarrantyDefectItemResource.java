package com.defectlist.inwarranty;

import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.NoDataFoundException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/app/v1/defects")
public class InwarrantyDefectItemResource {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CAPTCHA = "captcha";
    private static final String J_SESSION_ID = "id";
    private static final String SERVER_NAME = "server";
    private static final String INCLUDE_OTHER = "includeOther";
    private static final String ON = "on";

    private static final String UNKNOWN_ERROR = "<font color=red>Unknown error occurred. Please try after in few seconds.</font>" +
            "<br>";

    private final InwarrantyDefectItemService inwarrantyDefectItemService;

    @Autowired
    public InwarrantyDefectItemResource(final InwarrantyDefectItemService inwarrantyDefectItemService) {
        this.inwarrantyDefectItemService = inwarrantyDefectItemService;
    }

    @GetMapping()
    public String initialPage() {
        return inwarrantyDefectItemService.getPreload();
    }

    @GetMapping(path = "/login")
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
        } catch(final Exception exception) {
            return UNKNOWN_ERROR + exception.getMessage();
        }
    }

    @GetMapping("/items")
    public String getItems() {
        return inwarrantyDefectItemService.getDocuments();
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
