package com.defectlist.inwarranty.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class FinancialDetailsConverter implements DynamoDBTypeConverter<String, List<FinancialDetails>> {
    @Override
    public String convert(List<FinancialDetails> financialDetails) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String objectsString = objectMapper.writeValueAsString(financialDetails);
            return objectsString;
        } catch (JsonProcessingException e) {
            //do something
        }
        return null;
    }

    @Override
    public List<FinancialDetails> unconvert(String object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<FinancialDetails> objects = objectMapper.readValue(object, new TypeReference<List<FinancialDetails>>(){});
            return objects;
        } catch (JsonParseException e) {
            //do something
        } catch (JsonMappingException e) {
            //do something
        } catch (IOException e) {
            //do something
        }
        return null;
    }
}
