package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.relations.Reaction;
import com.rd.t1d.enums.Scope;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.LinkedList;
import java.util.List;

@Data
@Node("Post")
public class Post extends  BaseNode{

    @Property("title")
    @JsonProperty("title")
    private String title;

    @Property("content")
    @JsonProperty("content")
    private String content;

    @Property("image_id")
    @JsonProperty("image_id")
    private String image_id;

    @Property("video_url")
    @JsonProperty("video_url")
    private String video_url;

    /*@Property("post_type")
    @JsonProperty("post_type")
    private PostType postType;*/

    @Property("visibility_scope")
    @JsonProperty("visibility_scope")
    private Scope visibilityScope = Scope.PUBLIC;

    @Property("use_avatar")
    @JsonProperty("use_avatar")
    private Boolean useAvatar = Boolean.FALSE;

    @JsonProperty("tags")
    @Relationship(type="TAGGED_TO", direction = Relationship.Direction.OUTGOING)
    private List<Tag> tags = new LinkedList<>();

    @JsonProperty("response")
    @Relationship(type="POST_RESPONSE", direction = Relationship.Direction.OUTGOING)
    private List<Response> responses;

    @JsonProperty("reaction")
    @Relationship(type="POST_REACTED_BY", direction = Relationship.Direction.OUTGOING)
    private List<Reaction> reactions;

}
