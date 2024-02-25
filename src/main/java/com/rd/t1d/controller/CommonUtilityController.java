package com.rd.t1d.controller;

import com.rd.t1d.dto.CityDetails;
import com.rd.t1d.dto.UserPublicProfile;
import com.rd.t1d.service.CommonUtilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CommonUtilityController {

    @Autowired
    private CommonUtilityService commonUtilityService;

    @GetMapping("/city/{city-prefix}")
    public List<CityDetails> getCityDetails(@PathVariable("city-prefix") String cityPrefix){
        return commonUtilityService.getCityDetails(cityPrefix);
    }
}
