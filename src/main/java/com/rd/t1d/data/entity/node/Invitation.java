package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Invitation")
@Data
public class Invitation extends BaseNode {

    @Property("invitation_key")
    @JsonProperty("invitation_key")
    private String invitationKey;

    @Property("email")
    @JsonProperty("email")
    private String email;

    @Relationship(type = "INVITED_BY", direction = Relationship.Direction.OUTGOING)
    @JsonProperty("invited_by")
    private User invitedBy;
}
