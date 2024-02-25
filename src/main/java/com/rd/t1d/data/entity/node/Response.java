package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.relations.Reaction;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.List;

@Data
@Node("Response")
public class Response extends  BaseNode{

    @Property("content")
    @JsonProperty("content")
    private String content;

    @Property("image_url")
    @JsonProperty("image_url")
    private String image_url;

    @Property("use_avatar")
    @JsonProperty("use_avatar")
    private Boolean useAvatar = Boolean.FALSE;

    @JsonProperty("parent_response")
    @Relationship(type="REPLY_TO", direction = Relationship.Direction.OUTGOING)
    private Response parentResponse;

    @JsonProperty("reaction")
    @Relationship(type="RESPONSE_REACTED_BY", direction = Relationship.Direction.OUTGOING)
    private List<Reaction> reactions;

    @JsonProperty("responded_by")
    @Relationship(type="RESPONDED_BY", direction = Relationship.Direction.OUTGOING)
    private User respondedBy;

}
