package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class CityDetails {

    private String city;

    private String region;

    private String country;

    public CityDetails(String city, String region, String country) {
        this.city = city;
        this.region = region;
        this.country = country;
    }
}
