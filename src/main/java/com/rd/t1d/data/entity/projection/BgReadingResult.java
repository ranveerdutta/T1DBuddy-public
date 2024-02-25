package com.rd.t1d.data.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.enums.BgUnit;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class BgReadingResult implements Serializable {

    @JsonProperty("log_id")
    private Long logId;

    @JsonProperty("measured_at")
    private ZonedDateTime measuredAt;

    @JsonProperty("bg_number")
    private Double bgNumber;

    @JsonProperty("raw_bg")
    private Double rawBg;

    @JsonProperty("bg_unit")
    private BgUnit bgUnit;

    public BgReadingResult(Long logId, ZonedDateTime measuredAt, Double bgNumber, BgUnit bgUnit) {
        this.logId = logId;
        this.measuredAt = measuredAt;
        this.bgNumber = bgNumber;
        this.bgUnit = bgUnit;
    }

    public BgReadingResult(Long logId, ZonedDateTime measuredAt, Double bgNumber, Double rawBg, BgUnit bgUnit) {
        this.logId = logId;
        this.measuredAt = measuredAt;
        this.bgNumber = bgNumber;
        this.rawBg = rawBg;
        this.bgUnit = bgUnit;
    }
}
