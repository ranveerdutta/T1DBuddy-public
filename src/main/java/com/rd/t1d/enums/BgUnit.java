package com.rd.t1d.enums;

import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import org.springframework.http.HttpStatus;

public enum BgUnit {

    MGDL("mg/dl"),
    MMOL("mmol/l");

    private String displayStr;

    private BgUnit(String displayStr){
        this.displayStr = displayStr;
    }

    public static BgUnit getUnit(String name){
        for(BgUnit bgUnit : BgUnit.values()){
            if(bgUnit.name().equalsIgnoreCase(name)){
                return bgUnit;
            }
        }

        throw new T1DBuddyException(ErrorCode.WRONG_BG_UNIT, HttpStatus.BAD_REQUEST);
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
