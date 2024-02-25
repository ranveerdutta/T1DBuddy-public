package com.rd.t1d.data.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class AccessoryResult  implements Serializable {

    @JsonProperty("log_id")
    private Long logId;

    @JsonProperty("changed_at")
    private ZonedDateTime changedAt;

    @JsonProperty("accessory_type")
    private String accessoryType;

    public AccessoryResult(Long logId, ZonedDateTime changedAt, String accessoryType) {
        this.logId = logId;
        this.changedAt = changedAt;
        this.accessoryType = accessoryType;
    }
}
