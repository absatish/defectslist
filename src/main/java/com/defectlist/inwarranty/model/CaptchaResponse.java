package com.defectlist.inwarranty.model;

import lombok.Builder;
import lombok.Getter;

import org.springframework.http.HttpHeaders;

public class CaptchaResponse {

    public CaptchaResponse(final HttpHeaders httpHeaders, final byte[] imageBytes) {
        this.httpHeaders = httpHeaders;
        this.imageBytes =imageBytes;
    }

    private final byte[] imageBytes;

    private final HttpHeaders httpHeaders;

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

}
