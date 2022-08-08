package com.defectlist.inwarranty.ui;

public enum LineImage {

    VERTICAL_LINE_IMAGE("cut-vertical.jpg"),
    HORIZONTAL_LINE_IMAGE("cut.jpg");

    private final String name;

    LineImage(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
