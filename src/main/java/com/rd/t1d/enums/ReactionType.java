package com.rd.t1d.enums;

public enum ReactionType {

    LIKE("like"),
    DISLIKE("dislike"),
    SUPPORT("support"),
    LOVE("love");


    private String displayStr;

    private ReactionType(String displayStr){
        this.displayStr = displayStr;
    }

    public String getDisplayStr() {
        return displayStr;
    }

}

