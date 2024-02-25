package com.rd.t1d.enums;

import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import org.springframework.http.HttpStatus;

public enum Scope {

    //PRIVATE("private"),
    CONNECTION("connection"),
    //PUBLIC_WITH_AVATAR_NAME("public_with_avatar_name"),
    PUBLIC("public");

    private String code;

    private Scope(String name){
        this.code = name;
    }

    public static Scope getScope(String name){
        for(Scope scope : Scope.values()){
            if(scope.name().equalsIgnoreCase(name)){
                return scope;
            }
        }

        throw new T1DBuddyException(ErrorCode.WRONG_SCOPE, HttpStatus.BAD_REQUEST);
    }

}
