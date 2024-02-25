package com.rd.t1d.controller;

import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.service.RemoteBgService;
import com.rd.t1d.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class RemoteBgController {

    @Autowired
    private UserService userService;

    @Autowired
    private RemoteBgService remoteBgService;

    @GetMapping("/status")
    public ResponseEntity status(){
        Map<String, Object> map = new HashMap<>();
        map.put("status", "ok");
        map.put("name", "t1d-buddy");
        map.put("version", "v1");
        map.put("apiEnabled", true);
        map.put("careportalEnabled", true);
        map.put("boluscalcEnabled", true);
        ZonedDateTime now = ZonedDateTime.now();
        map.put("serverTime", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(now));
        map.put("serverTimeEpoch", now.toEpochSecond() * 1000);
        //map.put("settings", "settings");
        //map.put("extendedSettings", "extendedSettings");
        //map.put("authorized", "authorized");
        //map.put("runtimeState", "runtimeState");
        return new ResponseEntity<>( map, HttpStatus.OK);
    }

    @GetMapping("/verifyauth")
    public ResponseEntity verifyAuth(@RequestHeader("api-secret") String apiSecret){
        //log.info("api secret: " + apiSecret);
        User user = userService.getUserBySha1Secret(apiSecret);
        Map<String, Object> map = new HashMap<>();
        map.put("canRead", true);
        map.put("canWrite", true);
        map.put("isAdmin", true);
        map.put("message", user != null ? "OK": "UNAUTHORIZED");
        map.put("rolefound", "FOUND");
        map.put("permissions", "ROLE");
        return new ResponseEntity<>( map, HttpStatus.OK);
    }

    @PostMapping("/entries")
    public ResponseEntity entries(@RequestBody Object body, @RequestHeader("api-secret") String apiSecret){
        remoteBgService.addEntries(body, apiSecret);
        Map<String, Object> map = new HashMap<>();
        map.put("status", "ok");
        return new ResponseEntity<>( map, HttpStatus.OK);
    }

    @PostMapping("/treatments")
    public ResponseEntity treatments(@RequestBody Object body){
        /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("treatments: " + gson.toJson(body));*/

        Map<String, Object> map = new HashMap<>();
        map.put("status", "ok");
        return new ResponseEntity<>( map, HttpStatus.OK);
    }
}

