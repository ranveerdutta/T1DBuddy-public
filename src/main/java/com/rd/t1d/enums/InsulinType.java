package com.rd.t1d.enums;

public enum InsulinType {

    U40("40 IU/ML"),
    U100("100 IU/ML");

    private String displayStr;

    private InsulinType(String displayStr){
        this.displayStr = displayStr;
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
