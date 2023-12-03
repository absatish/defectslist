package com.defectlist.inwarranty.httprequestheaders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class ContentRequest {

    private final String jSessionId;

    private final String server;

    private final boolean callSearch;

    private final LocalDate fromDate;

    private final LocalDate toDate;

    public String getJSessionId() {
        return jSessionId;
    }

    public String getServer() {
        return server;
    }

    public boolean isCallSearch() {
        return callSearch;
    }

}
