package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.relations.FoodIncludes;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Node("Food")
public class Food {

    @Id
    @GeneratedValue
    private Long id;

    @Property("eaten_at")
    @JsonProperty("eaten_at")
    private ZonedDateTime eatenAt;

    @Relationship(type="INCLUDED_CONTENT", direction = Relationship.Direction.OUTGOING)
    @JsonProperty("included_contents")
    private List<FoodIncludes> includedContents;

}
