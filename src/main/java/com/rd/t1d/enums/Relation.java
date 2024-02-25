package com.rd.t1d.enums;

import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import org.springframework.http.HttpStatus;

public enum Relation {

    SELF("Self"),
    SIBLING("Sibling"),
    SPOUSE("Spouse"),
    FATHER("Father"),
    MOTHER("Mother"),
    SON("Son"),
    DAUGHTER("Daughter"),
    FRIEND("Friend");

    private String displayStr;

    private Relation(String displayStr){
        this.displayStr = displayStr;
    }

    public static Relation getRelation(String name){
        for(Relation relation : Relation.values()){
            if(relation.name().equalsIgnoreCase(name)){
                return relation;
            }
        }

        throw new T1DBuddyException(ErrorCode.WRONG_RELATION, HttpStatus.BAD_REQUEST);
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
