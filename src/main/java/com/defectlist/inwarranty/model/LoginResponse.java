package com.defectlist.inwarranty.model;

import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.util.List;

@Builder
@Getter
public class LoginResponse {

    private final boolean loginSuccess;

    private final List<String> complaintIds;

    private final int size;

    private final String errorMessage;

    private final String loggedInUser;

    private final String userId;

    private final URL horizontalImageUrl;

    private final URL verticalImageUrl;

    private final URL captchaImageUrl;

    private final String sessionId;

    private final String severId;

}
