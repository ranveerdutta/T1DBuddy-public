package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.enums.Status;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class ConnectionRequest  {

    @Id
    @GeneratedValue
    private Long id;

    @Property("requested_at")
    private ZonedDateTime requestedAt;

    @Property("status")
    private Status Status;

    @TargetNode
    private User targetUser;

    @JsonProperty("created_at")
    protected ZonedDateTime createdAt;

    @Property("updated_at")
    protected ZonedDateTime updatedAt;
}
