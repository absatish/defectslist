package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.defectlist.inwarranty.model.FinancialDetails;
import com.defectlist.inwarranty.model.FinancialMonth;
import com.defectlist.inwarranty.ui.TrackerUIPage;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FinanceTrackerService {

    private final DynamoDBService dynamoDBService;
    private final S3Service s3Service;

    @Autowired
    public FinanceTrackerService(final DynamoDBService dynamoDBService,
                                 final S3Service s3Service) {
        this.dynamoDBService = dynamoDBService;
        this.s3Service = s3Service;
    }

    public String getFinanceTrackerDetails(final String month) {
        FinancialMonth requiredMonth = getFinancialMonth(month);
        URL editImageUrl = s3Service.generatePresignedUrl("captcha-bucket", "edit-pencil-icon-0.jpg",
                Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        return TrackerUIPage.getDetailsPage(requiredMonth, editImageUrl);
    }

    public String create(final Map<String, String> requestParams) {
        final String month = RequestParameterResolver.getValue(requestParams, "month");
        final FinancialMonth financialMonth = getFinancialMonth(month);
        final List<FinancialDetails> existingDetails = financialMonth.getDetails();

        AtomicInteger maxInt = new AtomicInteger(existingDetails.stream()
                .mapToInt(FinancialDetails::getId)
                .max()
                .orElse(0));

        final CopyOnWriteArrayList<FinancialDetails> financialDetailsNew = RequestParameterResolver.getFinanceRecords(requestParams).stream()
                .map(details -> {
                    if (details.getId() == null) {
                        return details.toBuilder()
                                .id(maxInt.incrementAndGet())
                                .build();
                    } else {
                        return details;
                    }
                })
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

        financialDetailsNew.forEach(details -> existingDetails.stream()
                .filter(eDetails -> eDetails.getId().equals(details.getId()))
                .forEach(eDetails -> {
                    eDetails.setAmount(details.getAmount());
                    eDetails.setFinanceType(details.getFinanceType());
                    eDetails.setDescription(details.getDescription());
                    financialDetailsNew.removeIf(d -> d.getId().equals(details.getId()));
                }));

        final List<FinancialDetails> newDetails = new ArrayList<>();

        newDetails.addAll(financialDetailsNew);
        newDetails.addAll(existingDetails);

        financialMonth.setDetails(newDetails);

        dynamoDBService.save(financialMonth);

        return getFinanceTrackerDetails(month);
    }

    private FinancialMonth getFinancialMonth(final String month) {
        List<FinancialMonth> financialMonth = dynamoDBService.getFinancialDetails(month);
        FinancialMonth requiredMonth;
        if (financialMonth.isEmpty()) {
            requiredMonth = FinancialMonth.builder()
                    .month(month)
                    .details(Collections.emptyList())
                    .build();
        } else {
            requiredMonth = financialMonth.get(0);
        }
        return requiredMonth;
    }

}
