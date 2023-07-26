package com.defectlist.inwarranty.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class PaginatedRequest {

    private final String loggedInUserName;

    private final Map<String, String> complaintIds;

    private final UUID token;

    private final int pageNumber;

    public PaginatedRequest(final String loggedInUserName,
                            final Map<String, String> complaintIds,
                            final UUID token, final int pageNumber) {
        this.loggedInUserName = loggedInUserName;

        this.complaintIds = complaintIds;
        this.token = token;
        this.pageNumber = pageNumber;
    }
}
