package com.rd.t1d.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class T1DBuddyExceptionHandler {

    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_MSG = "error_msg";

    @ExceptionHandler(value = { T1DBuddyException.class })
    public ResponseEntity<Object> handleException(T1DBuddyException ex) {
        log.error("Error: ",ex);
        Map<String, String> resp = new HashMap<>();
        resp.put(ERROR_CODE, ex.getErrorCode().name());
        resp.put(ERROR_MSG, ex.getErrorCode().getMsg());
        return new ResponseEntity<Object>(resp, ex.getHttpStatus() != null ? ex.getHttpStatus(): HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Error: ",ex);
        Map<String, String> resp = new HashMap<>();
        resp.put(ERROR_CODE, ErrorCode.GENERIC_ERROR.name());
        resp.put(ERROR_MSG, ErrorCode.GENERIC_ERROR.getMsg());
        return new ResponseEntity<Object>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
