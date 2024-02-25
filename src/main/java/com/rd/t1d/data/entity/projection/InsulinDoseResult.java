package com.rd.t1d.data.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.enums.InsulinDoseType;
import com.rd.t1d.enums.InsulinType;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class InsulinDoseResult implements Serializable {

    @JsonProperty("log_id")
    private Long logId;

    @JsonProperty("taken_at")
    private ZonedDateTime takenAt;

    @JsonProperty("quantity")
    private Double quantity;

    @JsonProperty("insulin_type")
    private InsulinType insulinType;

    @JsonProperty("insulin_dose_type")
    private InsulinDoseType insulinDoseType;

    public InsulinDoseResult(Long logId, ZonedDateTime takenAt, Double quantity, InsulinType insulinType, InsulinDoseType insulinDoseType) {
        this.logId = logId;
        this.takenAt = takenAt;
        this.quantity = quantity;
        this.insulinType = insulinType;
        this.insulinDoseType = insulinDoseType;
    }
}
