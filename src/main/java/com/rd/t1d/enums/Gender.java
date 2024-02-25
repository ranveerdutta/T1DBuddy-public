package com.rd.t1d.enums;

public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private String displayStr;

    private Gender(String displayStr){
        this.displayStr = displayStr;
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
