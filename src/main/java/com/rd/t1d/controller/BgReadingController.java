package com.rd.t1d.controller;

import com.rd.t1d.data.entity.relations.BgReading;
import com.rd.t1d.data.entity.projection.BgReadingResult;
import com.rd.t1d.service.BgReadingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@Slf4j
public class BgReadingController {

    @Autowired
    private BgReadingService bgReadingService;

    @PostMapping("user/{email}/bg-reading")
    public ResponseEntity addBgReading(@PathVariable("email") String email, @RequestBody BgReading bgReading){
        bgReadingService.addBgReading(email, bgReading);
        return new ResponseEntity<>("BG Reading added successfully", HttpStatus.OK);
    }

    @GetMapping("user/{email}/bg-reading")
    public List<BgReadingResult> getBGReading(@PathVariable("email") String email,
                                              @RequestParam(required = true, name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
                                              @RequestParam(required = true, name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate){
        return bgReadingService.getBgReading(email, startDate, endDate);
    }
}
