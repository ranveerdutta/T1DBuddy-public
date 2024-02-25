package com.rd.t1d.controller;

import com.rd.t1d.data.entity.relations.BasalRate;
import com.rd.t1d.data.entity.relations.InsulinDoseTaken;
import com.rd.t1d.service.InsulinDoseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InsulinDoseController {

    @Autowired
    private InsulinDoseService insulinDoseService;

    @PostMapping("/user/{email}/insulin-dose")
    public ResponseEntity addInsulinDose(@PathVariable("email") String email, @RequestBody InsulinDoseTaken insulinDoseTaken){
        insulinDoseService.addInsulinDose(email, insulinDoseTaken);
        return new ResponseEntity<>("Insulin dose added successfully", HttpStatus.OK);
    }

    @GetMapping("/user/{email}/basal-rate/active")
    public List<BasalRate> getCurrentBasalRate(@PathVariable("email") String email){
        return insulinDoseService.getCurrentBasalRate(email);
    }

    @PostMapping("/user/{email}/basal-rate")
    public ResponseEntity addBasalRate(@PathVariable("email") String email, @RequestBody List<BasalRate> basalRateList){
        insulinDoseService.addBasalRate(email, basalRateList);
        return new ResponseEntity<>("Basal rate added successfully", HttpStatus.OK);
    }
}
