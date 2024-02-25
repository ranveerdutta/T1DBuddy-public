package com.rd.t1d.controller;

import com.rd.t1d.data.entity.relations.ExerciseDone;
import com.rd.t1d.service.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("user/{email}/exercise")
    public ResponseEntity addBgReading(@PathVariable("email") String email, @RequestBody ExerciseDone exercise){
        exerciseService.addExercise(email, exercise);
        return new ResponseEntity<>("Exercise added successfully", HttpStatus.OK);
    }
}
