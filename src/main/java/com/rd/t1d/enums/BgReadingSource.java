package com.rd.t1d.enums;

import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import org.springframework.http.HttpStatus;

public enum BgReadingSource {

    GLUCOMETER("glucometer"),
    CGM("cgm");

    private String displayStr;

    private BgReadingSource(String displayStr){
        this.displayStr = displayStr;
    }

    public static BgReadingSource getSource(String name){
        for(BgReadingSource source : BgReadingSource.values()){
            if(source.name().equalsIgnoreCase(name)){
                return source;
            }
        }

        throw new T1DBuddyException(ErrorCode.WRONG_BG_READING_SOURCE, HttpStatus.BAD_REQUEST);
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
