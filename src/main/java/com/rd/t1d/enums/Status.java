package com.rd.t1d.enums;

import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import org.springframework.http.HttpStatus;

public enum Status {

    REQUESTED("requested"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    CANCELED("canceled");

    private String displayStr;

    private Status(String displayStr){
        this.displayStr = displayStr;
    }

    public static Status getStatus(String name){
        for(Status status : Status.values()){
            if(status.name().equalsIgnoreCase(name)){
                return status;
            }
        }

        throw new T1DBuddyException(ErrorCode.WRONG_STATUS, HttpStatus.BAD_REQUEST);
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
