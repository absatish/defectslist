package com.defectlist.inwarranty.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "financial-tracker")
public class FinancialMonth {

    @DynamoDBHashKey(attributeName = "month")
    private String month;

    private List<FinancialDetails> details;

    @DynamoDBAttribute(attributeName = "details")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
    public List<FinancialDetails> getDetails() {
        return details;
    }

    public void setDetails(List<FinancialDetails> details) {
        this.details = details;
    }

}
