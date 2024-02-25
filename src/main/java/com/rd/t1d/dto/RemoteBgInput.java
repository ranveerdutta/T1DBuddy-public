package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@JsonIgnoreProperties
public class RemoteBgInput {

    @JsonProperty("date")
    private Long date;

    @JsonProperty("dateString")
    private String dateString;

    @JsonProperty("rssi")
    private Long rssi;

    @JsonProperty("device")
    private String device;

    @JsonProperty("direction")
    private String direction;

    @JsonProperty("rawbg")
    private Double rawbg;

    @JsonProperty("sgv")
    private Double sgv;

    @JsonProperty("type")
    private String type;
}
