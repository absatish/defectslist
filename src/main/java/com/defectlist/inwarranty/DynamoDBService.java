package com.defectlist.inwarranty;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.defectlist.inwarranty.model.FinancialDetails;
import com.defectlist.inwarranty.model.FinancialMonth;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DynamoDBService {

    private final String TABLE_NAME = "financial-tracker";

    private final DynamoDBMapper dynamoDBMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public DynamoDBService(final DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.objectMapper = new ObjectMapper();
    }

    public List<FinancialMonth> getFinancialDetails(final String month) {

//        DynamoDBScanExpression queryExpression = new DynamoDBScanExpression();
        Map<String, AttributeValue> expressionAttributes = new HashMap<>();
        expressionAttributes.put(":m", new AttributeValue().withS(month));

        Map<String, String> attributeNames = new HashMap<>();
        attributeNames.put("#month", "month");

        DynamoDBQueryExpression<FinancialMonth> queryExpression = new DynamoDBQueryExpression<FinancialMonth>()
                .withKeyConditionExpression("#month=:m")
                .withExpressionAttributeNames(attributeNames)
                .withExpressionAttributeValues(expressionAttributes);

        return dynamoDBMapper.query(FinancialMonth.class, queryExpression);
//        return dynamoDBMapper.scan(FinancialMonth.class, queryExpression);
    }

    public void save(final FinancialMonth financialMonth) {
        dynamoDBMapper.save(financialMonth);
    }
}
