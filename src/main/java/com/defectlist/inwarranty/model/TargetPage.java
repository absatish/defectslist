package com.defectlist.inwarranty.model;

import lombok.Getter;

@Getter
public enum TargetPage {
    DEFECTIVE("/app/v2/defects/login"),
    GOOD("/app/v2/defects/good-items");

    private final String target;

    TargetPage(String target) {
        this.target = target;
    }
}
