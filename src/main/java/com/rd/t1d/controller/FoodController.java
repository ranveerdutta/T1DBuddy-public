package com.rd.t1d.controller;

import com.rd.t1d.data.entity.node.Food;
import com.rd.t1d.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping("user/{email}/food-taken")
    public ResponseEntity addFoodTaken(@PathVariable("email") String email, @RequestBody Food foodTaken){
        foodService.addFood(email, foodTaken);
        return new ResponseEntity<>("Food details added successfully", HttpStatus.OK);
    }
}
