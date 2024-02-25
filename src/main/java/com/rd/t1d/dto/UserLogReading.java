package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.enums.UserLogType;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UserLogReading {

    @JsonProperty("log_id")
    private Long logId;

    @JsonProperty("log_time")
    private ZonedDateTime logTime;

    @JsonProperty("log_type")
    private UserLogType logType;
}
