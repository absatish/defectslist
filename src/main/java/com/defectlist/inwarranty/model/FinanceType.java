package com.defectlist.inwarranty.model;

import lombok.Getter;

@Getter
public enum FinanceType {

    PLUS(1),
    MINUS(-1),
    NEUTRAL(0);

    private final int multiplier;

    FinanceType(final int multiplier) {
        this.multiplier = multiplier;
    }


}
