package com.defectlist.inwarranty.model;

public class GridItem {

    private final String branchName;

    private final String complaintNumber;

    private final String date;

    private final String product;

    private final String model;

    private final String serialNumber;

    private final String dop;

    private final String spareName;

    private final String actualFault;

    private final String techName;

    private final String errorMessage;

    public String getComplaintNumber() {
        return this.complaintNumber;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getBranchName() {
        return this.branchName;
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

    public String getProduct() {
        return this.product;
    }

    public static class GridItemBuilder {

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

        private String errorMessage;

        public GridItemBuilder branchName(final String branchName) {
            this.branchName = branchName;
            return this;
        }

        public GridItemBuilder complaintNumber(final String complaintNumber) {
            this.complaintNumber = complaintNumber;
            return this;
        }

        public GridItemBuilder date(final String date) {
            this.date = date;
            return this;
        }

        public GridItemBuilder product(final String product) {
            this.product = product;
            return this;
        }

        public GridItemBuilder model(final String model) {
            this.model = model;
            return this;
        }

        public GridItemBuilder serialNumber(final String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public GridItemBuilder dop(final String dop) {
            this.dop = dop;
            return this;
        }

        public GridItemBuilder spareName(final String spareName) {
            this.spareName = spareName;
            return this;
        }

        public GridItemBuilder actualFault(final String actualFault) {
            this.actualFault = actualFault;
            return this;
        }

        public GridItemBuilder techName(final String techName) {
            this.techName = techName;
            return this;
        }

        public GridItemBuilder errorMessage(final String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public GridItem build() {
            return new GridItem(this);
        }

    }

    private GridItem(final GridItemBuilder gridItemBuilder) {
        this.branchName = gridItemBuilder.serialNumber;
        this.complaintNumber = gridItemBuilder.complaintNumber;
        this.date = gridItemBuilder.date;
        this.product = gridItemBuilder.product;
        this.model = gridItemBuilder.model;
        this.serialNumber = gridItemBuilder.serialNumber;
        this.dop = gridItemBuilder.dop;
        this.spareName = gridItemBuilder.spareName;
        this.actualFault = gridItemBuilder.actualFault;
        this.techName = gridItemBuilder.techName;
        this.errorMessage = gridItemBuilder.errorMessage;
    }

    public static GridItemBuilder builder() {
        return new GridItem.GridItemBuilder();
    }
}
