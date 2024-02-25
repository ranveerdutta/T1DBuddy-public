package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.InsulinDose;
import com.rd.t1d.enums.InsulinDoseType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class BasalRate {
    @Id
    @GeneratedValue
    private Long id;

    @Property("start_date")
    @JsonProperty("start_date")
    private ZonedDateTime startDate;

    @Property("end_date")
    @JsonProperty("end_date")
    private ZonedDateTime endDate;

    @Property("start_time")
    @JsonProperty("start_time")
    private LocalTime startTime;

    @Property("end_time")
    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("insulin_dose")
    @TargetNode
    private InsulinDose insulinDose;

    @Property("created_at")
    @JsonProperty("created_at")
    protected ZonedDateTime createdAt;

    @Property("updated_at")
    @JsonProperty("updated_at")
    protected ZonedDateTime updatedAt;

    public BasalRate(){

    }

    public BasalRate(Long id, ZonedDateTime startDate, ZonedDateTime endDate, LocalTime startTime, LocalTime endTime, InsulinDose insulinDose, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.insulinDose = insulinDose;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BasalRate(Long id, ZonedDateTime startDate, ZonedDateTime endDate, LocalTime startTime, LocalTime endTime, InsulinDose insulinDose) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.insulinDose = insulinDose;
    }
}
