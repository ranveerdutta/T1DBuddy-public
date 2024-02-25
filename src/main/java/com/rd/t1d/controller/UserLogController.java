package com.rd.t1d.controller;

import com.rd.t1d.dto.UserLogDetails;
import com.rd.t1d.dto.UserLogReading;
import com.rd.t1d.service.UserLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@Slf4j
public class UserLogController {

    @Autowired
    private UserLogService userLogService;

    @GetMapping("user/{email}/log")
    public UserLogDetails getUserLogData(@PathVariable("email") String email,
                                         @RequestParam(required = true, name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
                                         @RequestParam(required = true, name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate){
        return userLogService.getUserLogData(email, startDate, endDate);
    }

    @DeleteMapping("user/{email}/log")
    public ResponseEntity deleteUserLogData(@PathVariable("email") String email,
                                            @RequestBody UserLogReading userLogReading){
        userLogService.deleteUserLogReading(email, userLogReading);
        return new ResponseEntity<>("Log deleted successfully", HttpStatus.OK);
    }
}

