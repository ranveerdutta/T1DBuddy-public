package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.ExerciseType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class ExerciseDone {

    @Id
    @GeneratedValue
    private Long id;

    @Property("done_at")
    @JsonProperty("done_at")
    private ZonedDateTime doneAt;

    @JsonProperty("exercise_type")
    @TargetNode
    private ExerciseType exerciseType;

    @Property("duration_in_minutes")
    @JsonProperty("duration_in_minutes")
    private Long durationInMinutes;

    @Property("more_details")
    @JsonProperty("more_details")
    private String moreDetails;

}
