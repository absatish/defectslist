package com.sreeeservices.httprequestheaders;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContentRequest {

    public ContentRequest(final String jSessionId, final String server, final byte[] imageBytes) {
        this.jSessionId = jSessionId;
        this.server = server;
        this.imageBytes = imageBytes;
    }

    private final String jSessionId;

    private final String server;

    private final byte[] imageBytes;

    public String getJSessionId() {
        return jSessionId;
    }

    public String getServer() {
        return server;
    }

}
