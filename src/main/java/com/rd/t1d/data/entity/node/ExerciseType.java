package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Exercise_Type")
public class ExerciseType extends BaseNode{

    @Property("type")
    @JsonProperty("type")
    private String type;

}
