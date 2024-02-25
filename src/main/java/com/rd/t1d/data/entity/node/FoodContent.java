package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Node("Food_Content")
public class FoodContent {

    @Id
    @GeneratedValue
    private Long id;

    @Property("created_at")
    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @Property("content")
    @JsonProperty("content")
    private String content;

}
