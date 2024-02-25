package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.ReminderType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class Reminder {

    @Id
    @GeneratedValue
    private Long id;

    @Property("created_at")
    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @Property("updated_at")
    @JsonProperty("updated_at")
    private ZonedDateTime updatedAt;

    @Property("reminder_time")
    @JsonProperty("reminder_time")
    private ZonedDateTime reminderTime;

    @Property("status")
    @JsonProperty("status")
    private String status;

    @Property("more_details")
    @JsonProperty("more_details")
    private String moreDetails;

    @JsonProperty("reminder_type")
    @TargetNode
    private ReminderType reminderType;


    public Reminder() {
    }

    public Reminder(Long id, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime reminderTime, String status, String moreDetails) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reminderTime = reminderTime;
        this.status = status;
        this.moreDetails = moreDetails;
    }

    public Reminder(Long id, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime reminderTime, String status, String moreDetails, ReminderType reminderType) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reminderTime = reminderTime;
        this.status = status;
        this.moreDetails = moreDetails;
        this.reminderType = reminderType;
    }
}
