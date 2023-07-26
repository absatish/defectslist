package com.defectlist.inwarranty.utils;

import com.defectlist.inwarranty.S3Service;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoginDetailsValidator {

    private static final String USERNAME = "username";
    private static final String DEFAULT_USERNAME = "empty";
    private static final String PIN = "pin";
    private static final String DEFAULT_PIN = "0";

    private static final String UNDEFINED = "undefined";

    private final S3Service s3Service;

    @Autowired
    public LoginDetailsValidator(final S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public boolean validate(final String givenUsername, final Long givenPin) {
        final Map<String, String> credentials = s3Service.getCredentials();
        final String username = credentials.getOrDefault(USERNAME, DEFAULT_USERNAME);
        final Long pin = Long.valueOf(credentials.getOrDefault(PIN, DEFAULT_PIN));
        return !username.equals(DEFAULT_USERNAME) && givenUsername.equals(username)
                && !givenPin.equals(Long.valueOf(DEFAULT_PIN)) && givenPin.equals(pin);
    }
}
