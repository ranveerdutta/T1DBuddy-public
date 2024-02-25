package com.rd.t1d.controller;

import com.rd.t1d.data.entity.node.Record;
import com.rd.t1d.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/user/{email}/medical-record")
    public ResponseEntity createPost(@PathVariable("email") String email, @RequestBody Record record){
        recordService.addNewRecord(email, record);
        return new ResponseEntity<>("Record created successfully", HttpStatus.OK);
    }

    @GetMapping("/user/{email}/medical-record")
    public List<Record> getAllRecords(@PathVariable("email") String email){
        return recordService.getAllMedicalRecords(email);
    }
}
