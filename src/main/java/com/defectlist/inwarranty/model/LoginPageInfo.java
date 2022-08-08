package com.defectlist.inwarranty.model;

import lombok.Builder;
import lombok.Getter;

import java.net.URL;

@Builder(toBuilder = true)
@Getter
public class LoginPageInfo {

    private final URL captchaImageUrl;

    private final String sessionId;

    private final String serverId;

    private final String errorMessage;
}
