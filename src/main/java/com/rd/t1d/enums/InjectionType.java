package com.rd.t1d.enums;

public enum InjectionType {

    MDI("MDI"),
    INSULIN_PUMP("Insulin Pump");

    private String displayStr;

    private InjectionType(String displayStr){
        this.displayStr = displayStr;
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
