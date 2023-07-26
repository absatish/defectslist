package com.sreeeservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlenResponse {

    private int id;

    private String serviceBookingNumber;

    private String serviceBookingDate;

    private String assignDate;

    private String customerName;

    private String customerMobileNumber;

    private String state;

    private String city;

    private String pin;

    private String createdDate;

    private String status;

    private String assignStatus;

}
