package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.Post;
import com.rd.t1d.data.entity.node.Response;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.enums.ReactionType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class Reaction {

    @Id
    @GeneratedValue
    protected Long id;

    @Property("reacted_at")
    @JsonProperty("reacted_at")
    private ZonedDateTime reactedAt;

    @Property("reaction_type")
    @JsonProperty("reaction_type")
    private ReactionType reactionType;

    @TargetNode
    @JsonProperty("reacted_by")
    private User reactedBy;

}
