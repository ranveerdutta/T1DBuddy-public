package com.rd.t1d.controller;

import com.rd.t1d.data.entity.relations.AccessoryChanged;
import com.rd.t1d.service.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessoryController {

    @Autowired
    private AccessoryService accessoryService;

    @PostMapping("user/{email}/accessory-change")
    public ResponseEntity addAccessoryChange(@PathVariable("email") String email, @RequestBody AccessoryChanged accessoryChanged){
        accessoryService.addAccessoryChange(email, accessoryChanged);
        return new ResponseEntity<>("Accessory log added successfully", HttpStatus.OK);
    }

}
