package com.rd.t1d.utils;

public class StringUtils {

    public static String convertToUnderscoreString(String str){
        return str.toLowerCase().replace(" ", "_");
    }
}
