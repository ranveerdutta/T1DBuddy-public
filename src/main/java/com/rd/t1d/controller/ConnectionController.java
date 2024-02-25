package com.rd.t1d.controller;

import com.rd.t1d.dto.ConnectionInput;
import com.rd.t1d.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connection")
@Slf4j
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/request")
    public ResponseEntity invite(@RequestBody ConnectionInput connectionInput){
        connectionService.sendConnectionRequest(connectionInput);
        return new ResponseEntity<>("Connection request sent successfully", HttpStatus.OK);
    }

    @PostMapping("/respond")
    public ResponseEntity respond(@RequestBody ConnectionInput connectionInput){
        connectionService.respondConnectionRequest(connectionInput);
        return new ResponseEntity<>("Connection Request: "+ connectionInput.getStatus(), HttpStatus.OK);
    }
}
