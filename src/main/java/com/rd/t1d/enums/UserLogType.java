package com.rd.t1d.enums;

public enum UserLogType {

    BG("BG"),
    INSULIN("Insulin"),
    EXERCISE("Exercise"),
    //FOOD("Food"),
    ACCESSORY_CHANGE("Accessory change");

    private String displayStr;

    private UserLogType(String displayStr){
        this.displayStr = displayStr;
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
