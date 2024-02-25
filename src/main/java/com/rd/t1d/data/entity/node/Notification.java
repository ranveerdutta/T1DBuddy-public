package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.ZonedDateTime;

@Node("Notification")
@Data
public class Notification extends BaseNode{

    @Property("unique_id")
    @JsonProperty("unique_id")
    private Long uniqueId;

    @Property("notification_type")
    @JsonProperty("notification_type")
    private String notificationType;

    @Property("user_email")
    @JsonProperty("user_email")
    private String userEmail;

    @Property("title")
    @JsonProperty("title")
    private String title;

    @Property("body")
    @JsonProperty("body")
    private String body;

    @Property("notification_time")
    @JsonProperty("notification_time")
    private ZonedDateTime notificationTime;

    @Property("status")
    @JsonProperty("status")
    private String status;

    @Property("retry_count")
    @JsonProperty("retry_count")
    private Integer retryCount;

}
