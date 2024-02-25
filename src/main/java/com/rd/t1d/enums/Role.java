package com.rd.t1d.enums;

import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import org.springframework.http.HttpStatus;

public enum Role {

    ADMIN("Admin"),
    MEMBER("Member");

    private String displayStr;

    private Role(String displayStr){
        this.displayStr = displayStr;
    }

    public static Role getRole(String name){
        for(Role role : Role.values()){
            if(role.name().equalsIgnoreCase(name)){
                return role;
            }
        }

        throw new T1DBuddyException(ErrorCode.WRONG_ROLE, HttpStatus.BAD_REQUEST);
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
