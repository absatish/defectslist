package com.defectlist.inwarranty;

import com.defectlist.inwarranty.model.GridItem;
import com.defectlist.inwarranty.model.PaginatedRequest;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaginatedInwarrantyDefectItemService {

    private static final String DELIMITER_COMMA = ",";

    private final GridItemBuilderService gridItemBuilderService;

    private final boolean enablePagination;

    private final int maximumItemsPerPage;

    @Autowired
    public PaginatedInwarrantyDefectItemService(final GridItemBuilderService gridItemBuilderService,
                                                @Value("${pagination.active}") final boolean enablePagination,
                                                @Value("${pagination.max-items}") final int maximumItemsPerPage) {
        this.gridItemBuilderService = gridItemBuilderService;
        this.enablePagination = enablePagination;
        this.maximumItemsPerPage = maximumItemsPerPage;
    }

    public String getPaginatedGridItems(final PaginatedRequest paginatedRequest) {
        return getPaginatedGridItems(paginatedRequest.getComplaintIds(),
                paginatedRequest.getLoggedInUserName(), paginatedRequest.getPageNumber());
    }

    public String getPaginatedGridItems(final Map<String, String> callIds,
                                        final String loggedInUserName, final int pageNumber) {
        final Map<String, String> currentCallIds = new HashMap<>();
        final Map<String, String> nextCallList = new HashMap<>(callIds);
        final AtomicInteger count = new AtomicInteger();

        nextCallList.forEach((key, value) -> {
            if (count.get() < maximumItemsPerPage) {
                final List<String> complaintIds = Arrays.stream(value.split(DELIMITER_COMMA))
                        .filter(callId -> !callId.isEmpty())
                        .collect(Collectors.toList());
                final String complaintsListString;

                int desiredLength = Math.min(complaintIds.size(), maximumItemsPerPage);
                if (count.get() + desiredLength > maximumItemsPerPage) {
                    desiredLength = maximumItemsPerPage - count.get();
                }
                complaintsListString = getSublist(complaintIds, 0, desiredLength);
                count.set(count.get() + desiredLength);
                callIds.put(key, getSublist(complaintIds, desiredLength, complaintIds.size()));

                if (currentCallIds.get(key) != null) {
                    currentCallIds.put(key, currentCallIds.get(key).concat(DELIMITER_COMMA + complaintsListString));
                } else if (!complaintsListString.isEmpty()) {
                    currentCallIds.put(key, complaintsListString);
                }
            }
        });

        count.set(0);
        return gridItemBuilderService.getGridItems(currentCallIds, callIds, loggedInUserName, pageNumber);
    }

    private String getSublist(final List<String> complaintIds, final int startIndex, final int endIndex) {
        return complaintIds.subList(startIndex, endIndex).stream()
                .collect(Collectors.joining(DELIMITER_COMMA));
    }
}
