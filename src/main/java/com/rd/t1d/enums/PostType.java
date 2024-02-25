package com.rd.t1d.enums;

public enum PostType {

    BLOG("blog"),
    DISCUSSION("discussion"),
    COMMENT("comment"),
    REPLY("reply");

    private String displayStr;

    private PostType(String displayStr){
        this.displayStr = displayStr;
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
