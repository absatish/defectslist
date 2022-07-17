package com.defectlist.inwarranty.ui;

public enum MessageType {

    ERROR("error"),
    WARNING("warning"),
    INFO("info"),
    SUCCESS("success");

    private final String type;

    MessageType(final String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
