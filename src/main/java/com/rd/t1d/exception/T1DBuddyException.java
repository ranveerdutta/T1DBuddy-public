package com.rd.t1d.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class T1DBuddyException extends RuntimeException{

    private ErrorCode errorCode;

    private HttpStatus httpStatus;

    public T1DBuddyException(ErrorCode errorCode){
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public T1DBuddyException(ErrorCode errorCode, Throwable ex){
        super(errorCode.getMsg(), ex);
        this.errorCode = errorCode;
    }

    public T1DBuddyException(ErrorCode errorCode, HttpStatus httpStatus){
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public T1DBuddyException(ErrorCode errorCode, Throwable ex, HttpStatus httpStatus){
        super(errorCode.getMsg(), ex);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

}
