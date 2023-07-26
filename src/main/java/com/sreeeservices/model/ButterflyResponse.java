package com.sreeeservices.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ButterflyResponse {

    private String callId;

    private String customerName;

    private String contactNumber;

    private String product;

    private String address;

    private String brand;

    private String status;

    private String callType;

    private String officiallyAllocatedTo;

}
