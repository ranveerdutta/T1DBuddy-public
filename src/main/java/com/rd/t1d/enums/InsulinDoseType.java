package com.rd.t1d.enums;

public enum InsulinDoseType {

    BOLUS("bolus"),
    BASAL("basal"),
    PUMP_BASAL("pump_basal");

    private String displayStr;

    private InsulinDoseType(String displayStr){
        this.displayStr = displayStr;
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
