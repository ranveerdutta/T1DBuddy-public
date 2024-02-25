package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Tag")
public class Tag extends BaseNode{

    @Property("topic")
    @JsonProperty("topic")
    private String topic;

    @Property("topic_code")
    @JsonProperty("topic_code")
    private String topicCode;
}
