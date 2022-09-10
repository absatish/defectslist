package com.defectlist.inwarranty.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@Setter
@NoArgsConstructor
@DynamoDBDocument
public class FinancialDetails {

    @DynamoDBAttribute(attributeName = "id")
    private Integer id;

    @DynamoDBAttribute(attributeName = "amount")
    private Integer amount;

    @DynamoDBAttribute(attributeName = "description")
    private String description;

    @DynamoDBAttribute(attributeName = "financeType")
    private String financeType;

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getFinanceType() {
        return this.financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
