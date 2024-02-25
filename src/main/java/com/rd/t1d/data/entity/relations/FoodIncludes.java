package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.FoodContent;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

@Data
@RelationshipProperties
public class FoodIncludes {

    @Id
    @GeneratedValue
    protected Long id;

    @JsonProperty("quantity")
    @Property("quantity")
    private Integer quantity;

    @TargetNode
    @JsonProperty("food_content")
    private FoodContent foodContent;


}
