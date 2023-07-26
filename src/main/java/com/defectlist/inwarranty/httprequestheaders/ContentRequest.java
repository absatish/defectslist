package com.defectlist.inwarranty.httprequestheaders;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContentRequest {

    public ContentRequest(final String jSessionId, final String server) {
        this.jSessionId = jSessionId;
        this.server = server;
    }

    private final String jSessionId;

    private final String server;

    public String getJSessionId() {
        return jSessionId;
    }

    public String getServer() {
        return server;
    }

}
