package com.defectlist.inwarranty.model;

public class GridItem {

    private String branchName;

    private String complaintNumber;

    private String date;

    private String product;

    private String model;

    private String serialNumber;

    private String dop;

    private String spareName;

    private String actualFault;

    private String techName;

    public String getBranchName() {
        return this.branchName;
    }

    public String getComplaintNumber() {
        return this.complaintNumber;
    }

    public String getDate() {
        return this.date;
    }

    public String getModel() {
        return this.model;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public String getDop() {
        return this.dop;
    }

    public String getActualFault() {
        return this.actualFault;
    }

    public String getTechName() {
        return this.techName;
    }

    public String getSpareName() {
        return this.spareName;
    }

    public void setSpareName(final String spareName) {
        this.spareName = spareName;
    }

    public void setBranchName(final String branchName) {
        this.branchName = branchName;
    }

    public void setComplaintNumber(final String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setDop(final String dop) {
        this.dop = dop;
    }

    public void setActualFault(final String actualFault) {
        this.actualFault = actualFault;
    }

    public void setTechName(final String techName) {
        this.techName = techName;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct() {
        return this.product;
    }
}
