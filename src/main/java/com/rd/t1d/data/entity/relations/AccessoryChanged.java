package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.AccessoryType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class AccessoryChanged {

    @Id
    @GeneratedValue
    private Long id;

    @Property("changed_at")
    @JsonProperty("changed_at")
    private ZonedDateTime changedAt;

    @JsonProperty("accessory_type")
    @TargetNode
    private AccessoryType accessoryType;

    @Property("reminder_time")
    @JsonProperty("reminder_time")
    private ZonedDateTime reminderTime;

}
