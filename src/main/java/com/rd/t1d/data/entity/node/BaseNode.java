package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.ZonedDateTime;

@Data
public abstract class BaseNode {

    @Id
    @GeneratedValue
    protected Long id;

    @Property("created_at")
    @JsonProperty("created_at")
    protected ZonedDateTime createdAt;

    @Property("updated_at")
    @JsonProperty("updated_at")
    protected ZonedDateTime updatedAt;
}
